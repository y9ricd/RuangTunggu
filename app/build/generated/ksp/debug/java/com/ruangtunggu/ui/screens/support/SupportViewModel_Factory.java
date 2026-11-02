package com.ruangtunggu.ui.screens.support;

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
public final class SupportViewModel_Factory implements Factory<SupportViewModel> {
  private final Provider<QueueRepository> queueRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public SupportViewModel_Factory(Provider<QueueRepository> queueRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.queueRepositoryProvider = queueRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SupportViewModel get() {
    return newInstance(queueRepositoryProvider.get(), authRepositoryProvider.get());
  }

  public static SupportViewModel_Factory create(
      javax.inject.Provider<QueueRepository> queueRepositoryProvider,
      javax.inject.Provider<AuthRepository> authRepositoryProvider) {
    return new SupportViewModel_Factory(Providers.asDaggerProvider(queueRepositoryProvider), Providers.asDaggerProvider(authRepositoryProvider));
  }

  public static SupportViewModel_Factory create(Provider<QueueRepository> queueRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new SupportViewModel_Factory(queueRepositoryProvider, authRepositoryProvider);
  }

  public static SupportViewModel newInstance(QueueRepository queueRepository,
      AuthRepository authRepository) {
    return new SupportViewModel(queueRepository, authRepository);
  }
}
