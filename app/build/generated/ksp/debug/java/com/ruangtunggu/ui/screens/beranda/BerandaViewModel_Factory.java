package com.ruangtunggu.ui.screens.beranda;

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
public final class BerandaViewModel_Factory implements Factory<BerandaViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<QueueRepository> queueRepositoryProvider;

  public BerandaViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<QueueRepository> queueRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public BerandaViewModel get() {
    return newInstance(authRepositoryProvider.get(), queueRepositoryProvider.get());
  }

  public static BerandaViewModel_Factory create(
      javax.inject.Provider<AuthRepository> authRepositoryProvider,
      javax.inject.Provider<QueueRepository> queueRepositoryProvider) {
    return new BerandaViewModel_Factory(Providers.asDaggerProvider(authRepositoryProvider), Providers.asDaggerProvider(queueRepositoryProvider));
  }

  public static BerandaViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<QueueRepository> queueRepositoryProvider) {
    return new BerandaViewModel_Factory(authRepositoryProvider, queueRepositoryProvider);
  }

  public static BerandaViewModel newInstance(AuthRepository authRepository,
      QueueRepository queueRepository) {
    return new BerandaViewModel(authRepository, queueRepository);
  }
}
