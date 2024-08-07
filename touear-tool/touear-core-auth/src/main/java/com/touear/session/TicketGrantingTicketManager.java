package com.touear.session;

import com.touear.common.Expiration;
import com.touear.entity.BaseAccount;

import java.util.UUID;


/**
 * 登录凭证（TGT）管理抽象
 * 
 * @author Joe
 */
public interface TicketGrantingTicketManager extends Expiration {
	
    /**
     * 登录成功后，根据用户信息生成令牌
     * 
     * @param user
     * @return
     */
	default String generate(BaseAccount user) {
		String tgt = "third_login:TGT-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(tgt, user);
		return tgt;
	}
    
    /**
     * 登录成功后，根据用户信息生成令牌
     * 
     * @param user
     * @return
     */
    void create(String tgt, BaseAccount user);
    
    /**
     * 验证st是否存在且在有效期内，并更新过期时间戳
     * 
     * @param tgt
     * @return
     */
    BaseAccount get(String tgt);
    
    /**
     * 移除
     * 
     * @param tgt
     */
    void remove(String tgt);
}
