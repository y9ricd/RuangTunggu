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
public final class PilihDokterJadwalViewModel_Factory implements Factory<PilihDokterJadwalViewModel> {
  private final Provider<QueueRepository> queueRepositoryProvider;

  public PilihDokterJadwalViewModel_Factory(Provider<QueueRepository> queueRepositoryProvider) {
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public PilihDokterJadwalViewModel get() {
    return newInstance(queueRepositoryProvider.get());
  }

  public static PilihDokterJadwalViewModel_Factory create(
      javax.inject.Provider<QueueRepository> queueRepositoryProvider) {
    return new PilihDokterJadwalViewModel_Factory(Providers.asDaggerProvider(queueRepositoryProvider));
  }

  public static PilihDokterJadwalViewModel_Factory create(
      Provider<QueueRepository> queueRepositoryProvider) {
    return new PilihDokterJadwalViewModel_Factory(queueRepositoryProvider);
  }

  public static PilihDokterJadwalViewModel newInstance(QueueRepository queueRepository) {
    return new PilihDokterJadwalViewModel(queueRepository);
  }
}
