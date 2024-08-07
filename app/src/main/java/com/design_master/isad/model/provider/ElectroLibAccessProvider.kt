package com.design_master.isad.model.provider

class ElectroLibAccessProvider {
    external fun getServerBaseUrl(): String
    external fun getMasterKey(): String
    external fun getSecretKey(): String

    companion object {

        init {
            System.loadLibrary("electro_lib")
        }
    }
}
