package com.touear.starter.minio.model;

import io.minio.messages.Item;
import io.minio.messages.Owner;
import io.minio.messages.ResponseDate;
import lombok.Data;

/**
 * MinioItem
 *
 * @author Chen
 */
@Data
public class MinioItem {

	private String objectName;
	private ResponseDate lastModified;
	private String etag;
	private Long size;
	private String storageClass;
	private Owner owner;
	private boolean isDir;
	private String category;

	public MinioItem(Item item) {
		this.objectName = item.objectName();
		this.lastModified = new ResponseDate(item.lastModified());
		this.etag = item.etag();
		this.size = (long) item.size();
		this.storageClass = item.storageClass();
		this.owner = item.owner();
		this.isDir = item.isDir();
		this.category = isDir ? "dir" : "file";
	}

}
