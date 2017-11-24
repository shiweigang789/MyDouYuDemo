package com.swg.mydouyudemo.net.config;

import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;

/**
 * 网络参数配置
 * Created by swg on 2017/11/22.
 */

public final class NetWorkConfiguration {

    /**
     * 默认缓存
     */
    private boolean isCache;
    /**
     * 是否进行磁盘缓存
     */
    private boolean isDiskCache;
    /**
     * 是否进行内存缓存
     */
    private boolean isMemoryCache;
    /**
     * 内存缓存时间 单位S（默认60S）
     */
    private int memoryCacheTime;
    /**
     * 本地缓存时间 单位S （默认为4周）
     */
    private int diskCacheTime;
    /**
     * 本地缓存大小 单位字节 （默认为30M）
     */
    private int maxDiskCacheSize;
    /**
     * 本地缓存路径
     */
    private Cache diskCache;
    /**
     * 超时时间
     */
    private int connectTimeout;
    /**
     * 网络最大连接数
     */
    private ConnectionPool connectionPool;
    /**
     * 线程池线程数量
     */
    private int connectionCount;
    /**
     * Https客户端带证书访问
     */
    private InputStream[] certificates;
    public Context context;
    /**
     * 网络baseUrl
     */
    private String baseUrl;

    public NetWorkConfiguration(Context context) {
        this.isCache = true;
        this.isDiskCache = true;
        this.isMemoryCache = true;
        this.memoryCacheTime = 60;
        this.diskCacheTime = 28 * 24 * 60 * 60;
        this.maxDiskCacheSize = 30 * 1024 * 1024;
        this.diskCache = new Cache(context.getCacheDir(), maxDiskCacheSize);
        this.connectTimeout = 10000;
        this.connectionCount = 5;
        this.connectionPool = new ConnectionPool(connectionCount, connectTimeout, TimeUnit.SECONDS);
        this.certificates = null;
        this.context = context;
        this.baseUrl = null;
    }

    public boolean getIsCache() {
        return this.isCache;
    }

    public boolean getIsDiskCache() {
        return this.isDiskCache;
    }

    public boolean getIsMemoryCache() {
        return this.isMemoryCache;
    }

    public int getMaxDiskCacheSize() {
        return this.maxDiskCacheSize;
    }

    public int getmemoryCacheTime() {
        return this.memoryCacheTime;
    }

    public int getDiskCacheTime() {
        return this.diskCacheTime;
    }

    public Cache getDiskCache() {
        return this.diskCache;
    }

    public int getConnectTimeOut() {
        return this.connectTimeout;
    }

    public ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    public InputStream[] getCertificates() {
        return this.certificates;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * 设置是否进行缓存
     *
     * @param isCache
     * @return
     */
    public NetWorkConfiguration isCache(boolean isCache) {
        this.isCache = isCache;
        return this;
    }

    /**
     * 设置是否进行磁盘缓存
     *
     * @param isDiskCache
     * @return
     */
    public NetWorkConfiguration isDiskCache(boolean isDiskCache) {
        this.isDiskCache = isDiskCache;
        return this;
    }

    /**
     * 设置是否进行内存缓存
     *
     * @param isMemoryCache
     * @return
     */
    public NetWorkConfiguration isMemoryCache(boolean isMemoryCache) {
        this.isMemoryCache = isMemoryCache;
        return this;
    }

    /**
     * 设置内存缓存时间
     *
     * @param memoryCacheTime
     * @return
     */
    public NetWorkConfiguration setMemoryCacheTime(int memoryCacheTime) {
        if (!getIsMemoryCache()) {
            return this;
        }
        if (memoryCacheTime <= 0) {
            throw new IllegalArgumentException("configure memoryCacheTime exception!");
        }
        this.memoryCacheTime = memoryCacheTime;
        return this;
    }

    /**
     * 设置本地缓存时间
     *
     * @param diskCacheTime
     * @return
     */
    public NetWorkConfiguration setDiskCacheTime(int diskCacheTime) {
        if (!getIsDiskCache()) {
            return this;
        }
        if (diskCacheTime <= 0) {
            throw new IllegalArgumentException("configure diskCacheTime  exception!");
        }
        this.diskCacheTime = diskCacheTime;
        return this;
    }

    /**
     * 设置本地缓存路径和缓存空间
     *
     * @param saveFile
     * @param maxDiskCacheSize
     * @return
     */
    public NetWorkConfiguration setDiskCache(File saveFile, int maxDiskCacheSize) {
        if (!getIsDiskCache()) {
            return this;
        }
        if (!saveFile.exists() || maxDiskCacheSize <= 0) {
            throw new IllegalArgumentException("configure saveFile or maxDiskCacheSize exception!");
        }
        this.diskCache = null;
        this.diskCache = new Cache(saveFile, maxDiskCacheSize);
        return this;
    }

    /**
     * 设置网络连接超时时间
     *
     * @param connectTimeout
     * @return
     */
    public NetWorkConfiguration setConnectTimeout(int connectTimeout) {
        if (connectTimeout <= 0) {
            throw new IllegalArgumentException("configure connectTimeout exception!");
        }
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 设置网络连接池 线程个数和连接时间
     *
     * @param connectionCount
     * @param connectionTime
     * @param unit
     * @return
     */
    public NetWorkConfiguration setConnectPool(int connectionCount, int connectionTime, TimeUnit unit) {
        if (connectionCount <= 0 || connectionTime <= 0) {
            throw new IllegalArgumentException("configure connectionPool  exception!");
        }
        this.connectionPool = null;
        this.connectionPool = new ConnectionPool(connectionCount, connectionTime, unit);
        return this;
    }

    /**
     * 设置Https客户端带证书访问
     *
     * @param certificates
     * @return
     */
    public NetWorkConfiguration setCertificates(InputStream... certificates) {
        if (certificates == null) {
            throw new IllegalArgumentException("no certificates exception");
        }
        this.certificates = certificates;
        return this;
    }

    /**
     * 设置baseUrl
     *
     * @param baseUrl
     * @return
     */
    public NetWorkConfiguration setBaseUrl(String baseUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("NetWorkConfiguration no baseUrl exception");
        }
        this.baseUrl = baseUrl;
        return this;
    }

}
