package com.ruangtunggu.ui.screens.ticket;

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
public final class TicketQrViewModel_Factory implements Factory<TicketQrViewModel> {
  private final Provider<QueueRepository> queueRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public TicketQrViewModel_Factory(Provider<QueueRepository> queueRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.queueRepositoryProvider = queueRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public TicketQrViewModel get() {
    return newInstance(queueRepositoryProvider.get(), authRepositoryProvider.get());
  }

  public static TicketQrViewModel_Factory create(
      javax.inject.Provider<QueueRepository> queueRepositoryProvider,
      javax.inject.Provider<AuthRepository> authRepositoryProvider) {
    return new TicketQrViewModel_Factory(Providers.asDaggerProvider(queueRepositoryProvider), Providers.asDaggerProvider(authRepositoryProvider));
  }

  public static TicketQrViewModel_Factory create(Provider<QueueRepository> queueRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new TicketQrViewModel_Factory(queueRepositoryProvider, authRepositoryProvider);
  }

  public static TicketQrViewModel newInstance(QueueRepository queueRepository,
      AuthRepository authRepository) {
    return new TicketQrViewModel(queueRepository, authRepository);
  }
}
