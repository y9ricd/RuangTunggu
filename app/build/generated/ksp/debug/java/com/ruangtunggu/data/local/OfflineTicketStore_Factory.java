package com.ruangtunggu.data.local;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class OfflineTicketStore_Factory implements Factory<OfflineTicketStore> {
  private final Provider<Context> contextProvider;

  public OfflineTicketStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public OfflineTicketStore get() {
    return newInstance(contextProvider.get());
  }

  public static OfflineTicketStore_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new OfflineTicketStore_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static OfflineTicketStore_Factory create(Provider<Context> contextProvider) {
    return new OfflineTicketStore_Factory(contextProvider);
  }

  public static OfflineTicketStore newInstance(Context context) {
    return new OfflineTicketStore(context);
  }
}
