package com.moneycat.security;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PinAuthManager_Factory implements Factory<PinAuthManager> {
  private final Provider<CryptoManager> cryptoManagerProvider;

  public PinAuthManager_Factory(Provider<CryptoManager> cryptoManagerProvider) {
    this.cryptoManagerProvider = cryptoManagerProvider;
  }

  @Override
  public PinAuthManager get() {
    return newInstance(cryptoManagerProvider.get());
  }

  public static PinAuthManager_Factory create(Provider<CryptoManager> cryptoManagerProvider) {
    return new PinAuthManager_Factory(cryptoManagerProvider);
  }

  public static PinAuthManager newInstance(CryptoManager cryptoManager) {
    return new PinAuthManager(cryptoManager);
  }
}
