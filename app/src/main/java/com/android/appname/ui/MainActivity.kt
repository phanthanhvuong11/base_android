package com.android.appname.ui

import com.android.appname.R
import com.android.appname.arch.extensions.replaceFragment
import com.android.appname.arch.extensions.viewBinding
import com.android.appname.databinding.ActivityMainBinding
import com.android.appname.ui.base.BaseActivity
import com.android.appname.ui.home.HomeFragment

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
class MainActivity : BaseActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun afterViewCreated() {
        replaceFragment(R.id.frContainer, HomeFragment.newInstance())
    }
}
