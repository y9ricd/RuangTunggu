package com.ruangtunggu;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.ruangtunggu.data.local.OfflineTicketStore;
import com.ruangtunggu.data.repository.SupabaseAuthRepositoryImpl;
import com.ruangtunggu.data.repository.SupabaseQueueRepositoryImpl;
import com.ruangtunggu.di.SupabaseModule_ProvideSupabaseClientFactory;
import com.ruangtunggu.domain.usecase.LoginUseCase;
import com.ruangtunggu.domain.usecase.RegisterUseCase;
import com.ruangtunggu.ui.screens.beranda.BerandaViewModel;
import com.ruangtunggu.ui.screens.beranda.BerandaViewModel_HiltModules;
import com.ruangtunggu.ui.screens.beranda.BerandaViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.beranda.BerandaViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.CariRsPoliViewModel;
import com.ruangtunggu.ui.screens.booking.CariRsPoliViewModel_HiltModules;
import com.ruangtunggu.ui.screens.booking.CariRsPoliViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.CariRsPoliViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.KonfirmasiPendaftaranViewModel;
import com.ruangtunggu.ui.screens.booking.KonfirmasiPendaftaranViewModel_HiltModules;
import com.ruangtunggu.ui.screens.booking.KonfirmasiPendaftaranViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.KonfirmasiPendaftaranViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.PilihDokterJadwalViewModel;
import com.ruangtunggu.ui.screens.booking.PilihDokterJadwalViewModel_HiltModules;
import com.ruangtunggu.ui.screens.booking.PilihDokterJadwalViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.PilihDokterJadwalViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.PilihPoliViewModel;
import com.ruangtunggu.ui.screens.booking.PilihPoliViewModel_HiltModules;
import com.ruangtunggu.ui.screens.booking.PilihPoliViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.booking.PilihPoliViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.login.LoginViewModel;
import com.ruangtunggu.ui.screens.login.LoginViewModel_HiltModules;
import com.ruangtunggu.ui.screens.login.LoginViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.login.LoginViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.register.RegisterViewModel;
import com.ruangtunggu.ui.screens.register.RegisterViewModel_HiltModules;
import com.ruangtunggu.ui.screens.register.RegisterViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.register.RegisterViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.splash.SplashViewModel;
import com.ruangtunggu.ui.screens.splash.SplashViewModel_HiltModules;
import com.ruangtunggu.ui.screens.splash.SplashViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.splash.SplashViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.support.SupportViewModel;
import com.ruangtunggu.ui.screens.support.SupportViewModel_HiltModules;
import com.ruangtunggu.ui.screens.support.SupportViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.support.SupportViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.ui.screens.ticket.TicketQrViewModel;
import com.ruangtunggu.ui.screens.ticket.TicketQrViewModel_HiltModules;
import com.ruangtunggu.ui.screens.ticket.TicketQrViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.ruangtunggu.ui.screens.ticket.TicketQrViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.ruangtunggu.util.SystemNotificationHelper;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import io.github.jan.supabase.SupabaseClient;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerRuangTungguApp_HiltComponents_SingletonC {
  private DaggerRuangTungguApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public RuangTungguApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements RuangTungguApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public RuangTungguApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements RuangTungguApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public RuangTungguApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements RuangTungguApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public RuangTungguApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements RuangTungguApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RuangTungguApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements RuangTungguApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RuangTungguApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements RuangTungguApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public RuangTungguApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements RuangTungguApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public RuangTungguApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends RuangTungguApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends RuangTungguApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends RuangTungguApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends RuangTungguApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(10).put(BerandaViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, BerandaViewModel_HiltModules.KeyModule.provide()).put(CariRsPoliViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, CariRsPoliViewModel_HiltModules.KeyModule.provide()).put(KonfirmasiPendaftaranViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, KonfirmasiPendaftaranViewModel_HiltModules.KeyModule.provide()).put(LoginViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, LoginViewModel_HiltModules.KeyModule.provide()).put(PilihDokterJadwalViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, PilihDokterJadwalViewModel_HiltModules.KeyModule.provide()).put(PilihPoliViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, PilihPoliViewModel_HiltModules.KeyModule.provide()).put(RegisterViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, RegisterViewModel_HiltModules.KeyModule.provide()).put(SplashViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SplashViewModel_HiltModules.KeyModule.provide()).put(SupportViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SupportViewModel_HiltModules.KeyModule.provide()).put(TicketQrViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, TicketQrViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends RuangTungguApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<BerandaViewModel> berandaViewModelProvider;

    private Provider<CariRsPoliViewModel> cariRsPoliViewModelProvider;

    private Provider<KonfirmasiPendaftaranViewModel> konfirmasiPendaftaranViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<PilihDokterJadwalViewModel> pilihDokterJadwalViewModelProvider;

    private Provider<PilihPoliViewModel> pilihPoliViewModelProvider;

    private Provider<RegisterViewModel> registerViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<SupportViewModel> supportViewModelProvider;

    private Provider<TicketQrViewModel> ticketQrViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private LoginUseCase loginUseCase() {
      return new LoginUseCase(singletonCImpl.supabaseAuthRepositoryImplProvider.get());
    }

    private RegisterUseCase registerUseCase() {
      return new RegisterUseCase(singletonCImpl.supabaseAuthRepositoryImplProvider.get(), singletonCImpl.supabaseQueueRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.berandaViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.cariRsPoliViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.konfirmasiPendaftaranViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.pilihDokterJadwalViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.pilihPoliViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.registerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.supportViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.ticketQrViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(10).put(BerandaViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) berandaViewModelProvider)).put(CariRsPoliViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) cariRsPoliViewModelProvider)).put(KonfirmasiPendaftaranViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) konfirmasiPendaftaranViewModelProvider)).put(LoginViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) loginViewModelProvider)).put(PilihDokterJadwalViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) pilihDokterJadwalViewModelProvider)).put(PilihPoliViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) pilihPoliViewModelProvider)).put(RegisterViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) registerViewModelProvider)).put(SplashViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) splashViewModelProvider)).put(SupportViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) supportViewModelProvider)).put(TicketQrViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) ticketQrViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.ruangtunggu.ui.screens.beranda.BerandaViewModel 
          return (T) new BerandaViewModel(singletonCImpl.supabaseAuthRepositoryImplProvider.get(), singletonCImpl.supabaseQueueRepositoryImplProvider.get());

          case 1: // com.ruangtunggu.ui.screens.booking.CariRsPoliViewModel 
          return (T) new CariRsPoliViewModel(singletonCImpl.supabaseQueueRepositoryImplProvider.get());

          case 2: // com.ruangtunggu.ui.screens.booking.KonfirmasiPendaftaranViewModel 
          return (T) new KonfirmasiPendaftaranViewModel(singletonCImpl.supabaseAuthRepositoryImplProvider.get(), singletonCImpl.supabaseQueueRepositoryImplProvider.get());

          case 3: // com.ruangtunggu.ui.screens.login.LoginViewModel 
          return (T) new LoginViewModel(viewModelCImpl.loginUseCase());

          case 4: // com.ruangtunggu.ui.screens.booking.PilihDokterJadwalViewModel 
          return (T) new PilihDokterJadwalViewModel(singletonCImpl.supabaseQueueRepositoryImplProvider.get());

          case 5: // com.ruangtunggu.ui.screens.booking.PilihPoliViewModel 
          return (T) new PilihPoliViewModel(singletonCImpl.supabaseQueueRepositoryImplProvider.get());

          case 6: // com.ruangtunggu.ui.screens.register.RegisterViewModel 
          return (T) new RegisterViewModel(viewModelCImpl.registerUseCase());

          case 7: // com.ruangtunggu.ui.screens.splash.SplashViewModel 
          return (T) new SplashViewModel();

          case 8: // com.ruangtunggu.ui.screens.support.SupportViewModel 
          return (T) new SupportViewModel(singletonCImpl.supabaseQueueRepositoryImplProvider.get(), singletonCImpl.supabaseAuthRepositoryImplProvider.get());

          case 9: // com.ruangtunggu.ui.screens.ticket.TicketQrViewModel 
          return (T) new TicketQrViewModel(singletonCImpl.supabaseQueueRepositoryImplProvider.get(), singletonCImpl.supabaseAuthRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends RuangTungguApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends RuangTungguApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends RuangTungguApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SupabaseClient> provideSupabaseClientProvider;

    private Provider<OfflineTicketStore> offlineTicketStoreProvider;

    private Provider<SystemNotificationHelper> systemNotificationHelperProvider;

    private Provider<SupabaseAuthRepositoryImpl> supabaseAuthRepositoryImplProvider;

    private Provider<SupabaseQueueRepositoryImpl> supabaseQueueRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 1));
      this.offlineTicketStoreProvider = DoubleCheck.provider(new SwitchingProvider<OfflineTicketStore>(singletonCImpl, 2));
      this.systemNotificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<SystemNotificationHelper>(singletonCImpl, 3));
      this.supabaseAuthRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseAuthRepositoryImpl>(singletonCImpl, 0));
      this.supabaseQueueRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseQueueRepositoryImpl>(singletonCImpl, 4));
    }

    @Override
    public void injectRuangTungguApp(RuangTungguApp arg0) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.ruangtunggu.data.repository.SupabaseAuthRepositoryImpl 
          return (T) new SupabaseAuthRepositoryImpl(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.offlineTicketStoreProvider.get(), singletonCImpl.systemNotificationHelperProvider.get());

          case 1: // io.github.jan.supabase.SupabaseClient 
          return (T) SupabaseModule_ProvideSupabaseClientFactory.provideSupabaseClient();

          case 2: // com.ruangtunggu.data.local.OfflineTicketStore 
          return (T) new OfflineTicketStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.ruangtunggu.util.SystemNotificationHelper 
          return (T) new SystemNotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.ruangtunggu.data.repository.SupabaseQueueRepositoryImpl 
          return (T) new SupabaseQueueRepositoryImpl(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.offlineTicketStoreProvider.get(), singletonCImpl.systemNotificationHelperProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
