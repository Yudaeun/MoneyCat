package com.moneycat.security;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ScreenSecurityManager_Factory implements Factory<ScreenSecurityManager> {
  @Override
  public ScreenSecurityManager get() {
    return newInstance();
  }

  public static ScreenSecurityManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ScreenSecurityManager newInstance() {
    return new ScreenSecurityManager();
  }

  private static final class InstanceHolder {
    private static final ScreenSecurityManager_Factory INSTANCE = new ScreenSecurityManager_Factory();
  }
}
