package com.ruangtunggu.ui.screens.booking;

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
public final class PilihPoliViewModel_Factory implements Factory<PilihPoliViewModel> {
  private final Provider<QueueRepository> queueRepositoryProvider;

  public PilihPoliViewModel_Factory(Provider<QueueRepository> queueRepositoryProvider) {
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public PilihPoliViewModel get() {
    return newInstance(queueRepositoryProvider.get());
  }

  public static PilihPoliViewModel_Factory create(
      javax.inject.Provider<QueueRepository> queueRepositoryProvider) {
    return new PilihPoliViewModel_Factory(Providers.asDaggerProvider(queueRepositoryProvider));
  }

  public static PilihPoliViewModel_Factory create(
      Provider<QueueRepository> queueRepositoryProvider) {
    return new PilihPoliViewModel_Factory(queueRepositoryProvider);
  }

  public static PilihPoliViewModel newInstance(QueueRepository queueRepository) {
    return new PilihPoliViewModel(queueRepository);
  }
}
