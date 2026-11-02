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
public final class CariRsPoliViewModel_Factory implements Factory<CariRsPoliViewModel> {
  private final Provider<QueueRepository> queueRepositoryProvider;

  public CariRsPoliViewModel_Factory(Provider<QueueRepository> queueRepositoryProvider) {
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public CariRsPoliViewModel get() {
    return newInstance(queueRepositoryProvider.get());
  }

  public static CariRsPoliViewModel_Factory create(
      javax.inject.Provider<QueueRepository> queueRepositoryProvider) {
    return new CariRsPoliViewModel_Factory(Providers.asDaggerProvider(queueRepositoryProvider));
  }

  public static CariRsPoliViewModel_Factory create(
      Provider<QueueRepository> queueRepositoryProvider) {
    return new CariRsPoliViewModel_Factory(queueRepositoryProvider);
  }

  public static CariRsPoliViewModel newInstance(QueueRepository queueRepository) {
    return new CariRsPoliViewModel(queueRepository);
  }
}
