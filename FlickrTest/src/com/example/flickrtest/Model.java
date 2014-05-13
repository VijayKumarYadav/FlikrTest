package com.example.flickrtest;

/**
 * Data container for Images
 * @author VijayK
 *
 */
public class Model {

	/** Image Title */
    private String title;
    
    /** Image owner name */
    private String authorName;
    
    /** Image Id */
    private String id;
    
    /** Farm Id */
    private String farmId;
    
    /** Secret Id */
    private String secretId;
    
    /** Server Id */
    private String serverId;
    
    /**
	 * @return the farmId
	 */
	public String getFarmId() {
		return farmId;
	}
	/**
	 * @param farmId the farmId to set
	 */
	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}
	/**
	 * @return the secretId
	 */
	public String getSecretId() {
		return secretId;
	}
	/**
	 * @param secretId the secretId to set
	 */
	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}
	/**
	 * @return the serverId
	 */
	public String getServerId() {
		return serverId;
	}
	/**
	 * @param serverId the serverId to set
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}
	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
