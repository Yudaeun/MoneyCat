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
public final class RootDetector_Factory implements Factory<RootDetector> {
  @Override
  public RootDetector get() {
    return newInstance();
  }

  public static RootDetector_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RootDetector newInstance() {
    return new RootDetector();
  }

  private static final class InstanceHolder {
    private static final RootDetector_Factory INSTANCE = new RootDetector_Factory();
  }
}
