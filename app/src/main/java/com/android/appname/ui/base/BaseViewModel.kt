package com.android.appname.ui.base

import androidx.lifecycle.ViewModel
import com.android.appname.arch.extensions.LoadingAware
import com.android.appname.arch.extensions.ViewErrorAware

/**
 * @author mvn-vuongphan-dn 10/14/22
 */
open class BaseViewModel : ViewModel(), ViewErrorAware, LoadingAware
