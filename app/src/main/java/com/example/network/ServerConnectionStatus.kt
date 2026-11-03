package com.example.network

data class ServerConnectionStatus(
    val isConnected: Boolean = true,
    val serverUrl: String = "https://ais-dev-h2viaw72ek4gcw6vmqzuv6-412611599102.europe-west2.run.app",
    val pingLatencyMs: Long = 48L,
    val lastSyncTimeFa: String = "هم‌اکنون",
    val activeNodesCount: Int = 34, // 34 provinces synchronized
    val serverVersion: String = "v2.4.0-enterprise",
    val isSyncing: Boolean = false,
    val syncErrorMessage: String? = null
)

data class OnlineServerStats(
    val totalOrdersCount: Int,
    val totalVolumeAfn: Long,
    val activeVendorsCount: Int,
    val synchronizedProvinces: Int,
    val serverCpuLoad: String,
    val databaseStatus: String
)
