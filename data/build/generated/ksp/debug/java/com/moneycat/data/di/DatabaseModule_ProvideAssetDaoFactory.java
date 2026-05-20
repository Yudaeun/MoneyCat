package com.moneycat.data.di;

import com.moneycat.data.local.db.MoneyCatDatabase;
import com.moneycat.data.local.db.dao.AssetDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideAssetDaoFactory implements Factory<AssetDao> {
  private final Provider<MoneyCatDatabase> dbProvider;

  public DatabaseModule_ProvideAssetDaoFactory(Provider<MoneyCatDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AssetDao get() {
    return provideAssetDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAssetDaoFactory create(
      Provider<MoneyCatDatabase> dbProvider) {
    return new DatabaseModule_ProvideAssetDaoFactory(dbProvider);
  }

  public static AssetDao provideAssetDao(MoneyCatDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAssetDao(db));
  }
}
