package com.ruangtunggu.domain.usecase;

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
public final class RegisterUseCase_Factory implements Factory<RegisterUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<QueueRepository> queueRepositoryProvider;

  public RegisterUseCase_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<QueueRepository> queueRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public RegisterUseCase get() {
    return newInstance(authRepositoryProvider.get(), queueRepositoryProvider.get());
  }

  public static RegisterUseCase_Factory create(
      javax.inject.Provider<AuthRepository> authRepositoryProvider,
      javax.inject.Provider<QueueRepository> queueRepositoryProvider) {
    return new RegisterUseCase_Factory(Providers.asDaggerProvider(authRepositoryProvider), Providers.asDaggerProvider(queueRepositoryProvider));
  }

  public static RegisterUseCase_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<QueueRepository> queueRepositoryProvider) {
    return new RegisterUseCase_Factory(authRepositoryProvider, queueRepositoryProvider);
  }

  public static RegisterUseCase newInstance(AuthRepository authRepository,
      QueueRepository queueRepository) {
    return new RegisterUseCase(authRepository, queueRepository);
  }
}
