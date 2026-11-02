package com.ruangtunggu.domain.usecase;

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
public final class ObserveActiveQueueUseCase_Factory implements Factory<ObserveActiveQueueUseCase> {
  private final Provider<QueueRepository> queueRepositoryProvider;

  public ObserveActiveQueueUseCase_Factory(Provider<QueueRepository> queueRepositoryProvider) {
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public ObserveActiveQueueUseCase get() {
    return newInstance(queueRepositoryProvider.get());
  }

  public static ObserveActiveQueueUseCase_Factory create(
      javax.inject.Provider<QueueRepository> queueRepositoryProvider) {
    return new ObserveActiveQueueUseCase_Factory(Providers.asDaggerProvider(queueRepositoryProvider));
  }

  public static ObserveActiveQueueUseCase_Factory create(
      Provider<QueueRepository> queueRepositoryProvider) {
    return new ObserveActiveQueueUseCase_Factory(queueRepositoryProvider);
  }

  public static ObserveActiveQueueUseCase newInstance(QueueRepository queueRepository) {
    return new ObserveActiveQueueUseCase(queueRepository);
  }
}
