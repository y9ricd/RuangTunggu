package com.ruangtunggu.ui.screens.booking;

import com.ruangtunggu.domain.repository.AuthRepository;
import com.ruangtunggu.domain.repository.QueueRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class KonfirmasiPendaftaranViewModel_Factory implements Factory<KonfirmasiPendaftaranViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<QueueRepository> queueRepositoryProvider;

  public KonfirmasiPendaftaranViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<QueueRepository> queueRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public KonfirmasiPendaftaranViewModel get() {
    return newInstance(authRepositoryProvider.get(), queueRepositoryProvider.get());
  }

  public static KonfirmasiPendaftaranViewModel_Factory create(
      javax.inject.Provider<AuthRepository> authRepositoryProvider,
      javax.inject.Provider<QueueRepository> queueRepositoryProvider) {
    return new KonfirmasiPendaftaranViewModel_Factory(Providers.asDaggerProvider(authRepositoryProvider), Providers.asDaggerProvider(queueRepositoryProvider));
  }

  public static KonfirmasiPendaftaranViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<QueueRepository> queueRepositoryProvider) {
    return new KonfirmasiPendaftaranViewModel_Factory(authRepositoryProvider, queueRepositoryProvider);
  }

  public static KonfirmasiPendaftaranViewModel newInstance(AuthRepository authRepository,
      QueueRepository queueRepository) {
    return new KonfirmasiPendaftaranViewModel(authRepository, queueRepository);
  }
}
