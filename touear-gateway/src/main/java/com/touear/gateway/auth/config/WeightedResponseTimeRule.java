package com.touear.gateway.auth.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.client.config.IClientConfigKey;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Title: WeightedResponseTimeRule.java
 * @Description: WeightedResponseTimeRule
 * @author chenl
 * @date 2021-01-21 09:22:32
 * @version 1.0
 */
@Slf4j
public class WeightedResponseTimeRule extends RoundRobinRule {
    public static final IClientConfigKey<Integer> WEIGHT_TASK_TIMER_INTERVAL_CONFIG_KEY = new IClientConfigKey<Integer>() {
        public String key() {
            return "ServerWeightTaskTimerInterval";
        }

        public String toString() {
            return this.key();
        }

        public Class<Integer> type() {
            return Integer.class;
        }
    };
    public static final int DEFAULT_TIMER_INTERVAL = 30000;
    private int serverWeightTaskTimerInterval = 30000;
    private volatile List<Double> accumulatedWeights = new ArrayList<Double>();
    private final Random random = new Random();
    protected Timer serverWeightTimer = null;
    protected AtomicBoolean serverWeightAssignmentInProgress = new AtomicBoolean(false);
    String name = "unknown";

    public WeightedResponseTimeRule() {
    }

    public WeightedResponseTimeRule(ILoadBalancer lb) {
        super(lb);
    }

    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        if (lb instanceof BaseLoadBalancer) {
            this.name = ((BaseLoadBalancer)lb).getName();
        }

        this.initialize(lb);
    }

    void initialize(ILoadBalancer lb) {
        if (this.serverWeightTimer != null) {
            this.serverWeightTimer.cancel();
        }

        this.serverWeightTimer = new Timer("NFLoadBalancer-serverWeightTimer-" + this.name, true);
       	//启动一个定时任务，用来为每个服务实例计算权重，改任务默认30秒执行一次
        this.serverWeightTimer.schedule(new DynamicServerWeightTask(), 0L, (long)this.serverWeightTaskTimerInterval);
        ServerWeight sw = new ServerWeight();
        sw.maintainWeights();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	WeightedResponseTimeRule.log.info("Stopping NFLoadBalancer-serverWeightTimer-" + WeightedResponseTimeRule.this.name);
                WeightedResponseTimeRule.this.serverWeightTimer.cancel();
            }
        }));
    }

    public void shutdown() {
        if (this.serverWeightTimer != null) {
        	log.info("Stopping NFLoadBalancer-serverWeightTimer-" + this.name);
            this.serverWeightTimer.cancel();
        }

    }

    List<Double> getAccumulatedWeights() {
        return Collections.unmodifiableList(this.accumulatedWeights);
    }

    @SuppressWarnings({"RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE"})
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        } else {
            Server server = null;

            while(server == null) {
                List<Double> currentWeights = this.accumulatedWeights;
                if (Thread.interrupted()) {
                    return null;
                }

                List<Server> allList = lb.getAllServers();
                int serverCount = allList.size();
                if (serverCount == 0) {
                    return null;
                }

                int serverIndex = 0;
                //获取最后一个实例的权重
                double maxTotalWeight = currentWeights.size() == 0 ? 0.0D : (Double)currentWeights.get(currentWeights.size() - 1);
                //最后一个实例的权重值大于 0.001进
                if (maxTotalWeight >= 0.001D && serverCount == currentWeights.size()) {
                	//产生一个[0,maxTotalWeight]的随机数
                    double randomWeight = this.random.nextDouble() * maxTotalWeight;
                    int n = 0;

					//遍历维护的权重清单，若权重值大于等于随机得到的数值，就选择这个实例
                    for(Iterator var13 = currentWeights.iterator(); var13.hasNext(); ++n) {
                        Double d = (Double)var13.next();
                        if (d >= randomWeight) {
                            serverIndex = n;
                            break;
                        }
                    }

                    server = (Server)allList.get(serverIndex);
                } else {//否则调用父类的的线性轮询策略
                    server = super.choose(this.getLoadBalancer(), key);
                    if (server == null) {
                        return server;
                    }
                }

                if (server == null) {
                    Thread.yield();
                } else {
                    if (server.isAlive()) {
                        return server;
                    }

                    server = null;
                }
            }

            return server;
        }
    }

    void setWeights(List<Double> weights) {
        this.accumulatedWeights = weights;
    }

    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
        this.serverWeightTaskTimerInterval = (Integer)clientConfig.get(WEIGHT_TASK_TIMER_INTERVAL_CONFIG_KEY, 30000);
    }

    class ServerWeight {
    	 
        public void maintainWeights() {
            ILoadBalancer lb = getLoadBalancer();
            if (lb == null) {
                return;
            }
            if (serverWeightAssignmentInProgress.get()) {
                return; // Ping in progress - nothing to do
            } else {
                serverWeightAssignmentInProgress.set(true);
            }
            try {
                log.info("Weight adjusting job started");
                AbstractLoadBalancer nlb = (AbstractLoadBalancer) lb;
                LoadBalancerStats stats = nlb.getLoadBalancerStats();
                if (stats == null) {
                    // no statistics, nothing to do
                    return;
                }
                double totalResponseTime = 0;
                // find maximal 95% response time
                for (Server server : nlb.getServerList(false)) {
                    // this will automatically load the stats if not in cache
                    ServerStats ss = stats.getSingleServerStat(server);
                    totalResponseTime += ss.getResponseTimeAvg();
                }
                // weight for each server is (sum of responseTime of all servers - responseTime)
                // so that the longer the response time, the less the weight and the less likely to be chosen
                Double weightSoFar = 0.0;
                
                // create new list and hot swap the reference
                List<Double> finalWeights = new ArrayList<Double>();
                for (Server server : nlb.getServerList(false)) {
                    ServerStats ss = stats.getSingleServerStat(server);
                    double weight = totalResponseTime - ss.getResponseTimeAvg();
                    weightSoFar += weight;
                    finalWeights.add(weightSoFar);   
                }
                setWeights(finalWeights);
            } catch (Throwable t) {
            	log.error("Exception while dynamically calculating server weights", t);
            } finally {
                serverWeightAssignmentInProgress.set(false);
            }
     
        }
    }

    class DynamicServerWeightTask extends TimerTask {
        DynamicServerWeightTask() {
        }

        public void run() {
            ServerWeight serverWeight = WeightedResponseTimeRule.this.new ServerWeight();

            try {
                serverWeight.maintainWeights();
            } catch (Exception var3) {
                WeightedResponseTimeRule.log.error("Error running DynamicServerWeightTask for {}", WeightedResponseTimeRule.this.name, var3);
            }

        }
    }
}