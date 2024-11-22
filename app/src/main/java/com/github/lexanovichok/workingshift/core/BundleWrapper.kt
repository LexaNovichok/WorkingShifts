package com.github.lexanovichok.workingshift.core

import android.os.Bundle

interface BundleWrapper {

    interface Mutable : Save, Restore

    interface Save {
        fun save(last: ArrayList<String>)
    }

    interface Restore {
        fun restore(): ArrayList<String>
    }

    class Base(
        private val bundle: Bundle
    ) : Mutable {

        override fun restore(): ArrayList<String> {
            return bundle.getStringArrayList(KEY) ?: ArrayList()
        }

        override fun save(last: ArrayList<String>) {
            bundle.putSerializable(KEY, last)
        }

        companion object {
            private const val KEY = "USER_DATA_BUNDLE_KEY"
        }
    }
}