package com.moneycat.data.repository;

import com.moneycat.data.local.db.dao.UserProfileDao;
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
public final class UserProfileRepositoryImpl_Factory implements Factory<UserProfileRepositoryImpl> {
  private final Provider<UserProfileDao> daoProvider;

  public UserProfileRepositoryImpl_Factory(Provider<UserProfileDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public UserProfileRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static UserProfileRepositoryImpl_Factory create(Provider<UserProfileDao> daoProvider) {
    return new UserProfileRepositoryImpl_Factory(daoProvider);
  }

  public static UserProfileRepositoryImpl newInstance(UserProfileDao dao) {
    return new UserProfileRepositoryImpl(dao);
  }
}
