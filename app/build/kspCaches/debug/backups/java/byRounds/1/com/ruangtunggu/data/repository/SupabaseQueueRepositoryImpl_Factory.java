package com.ruangtunggu.data.repository;

import com.ruangtunggu.data.local.OfflineTicketStore;
import com.ruangtunggu.util.SystemNotificationHelper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class SupabaseQueueRepositoryImpl_Factory implements Factory<SupabaseQueueRepositoryImpl> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private final Provider<OfflineTicketStore> offlineTicketStoreProvider;

  private final Provider<SystemNotificationHelper> notificationHelperProvider;

  public SupabaseQueueRepositoryImpl_Factory(Provider<SupabaseClient> supabaseClientProvider,
      Provider<OfflineTicketStore> offlineTicketStoreProvider,
      Provider<SystemNotificationHelper> notificationHelperProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
    this.offlineTicketStoreProvider = offlineTicketStoreProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public SupabaseQueueRepositoryImpl get() {
    return newInstance(supabaseClientProvider.get(), offlineTicketStoreProvider.get(), notificationHelperProvider.get());
  }

  public static SupabaseQueueRepositoryImpl_Factory create(
      javax.inject.Provider<SupabaseClient> supabaseClientProvider,
      javax.inject.Provider<OfflineTicketStore> offlineTicketStoreProvider,
      javax.inject.Provider<SystemNotificationHelper> notificationHelperProvider) {
    return new SupabaseQueueRepositoryImpl_Factory(Providers.asDaggerProvider(supabaseClientProvider), Providers.asDaggerProvider(offlineTicketStoreProvider), Providers.asDaggerProvider(notificationHelperProvider));
  }

  public static SupabaseQueueRepositoryImpl_Factory create(
      Provider<SupabaseClient> supabaseClientProvider,
      Provider<OfflineTicketStore> offlineTicketStoreProvider,
      Provider<SystemNotificationHelper> notificationHelperProvider) {
    return new SupabaseQueueRepositoryImpl_Factory(supabaseClientProvider, offlineTicketStoreProvider, notificationHelperProvider);
  }

  public static SupabaseQueueRepositoryImpl newInstance(SupabaseClient supabaseClient,
      OfflineTicketStore offlineTicketStore, SystemNotificationHelper notificationHelper) {
    return new SupabaseQueueRepositoryImpl(supabaseClient, offlineTicketStore, notificationHelper);
  }
}
