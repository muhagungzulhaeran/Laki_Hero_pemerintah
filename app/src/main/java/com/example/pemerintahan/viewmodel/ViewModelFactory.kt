package com.example.pemerintahan.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pemerintahan.data.remote.UserRepository
import com.example.pemerintahan.di.Injection

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: UserRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(SignInViewModel::class.java) ->{
                SignInViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java)-> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java)->{
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileMenuViewModel::class.java)->{
                ProfileMenuViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java)->{
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RiwayatLaporanViewModel::class.java)->{
                RiwayatLaporanViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ActionLaporViewModel::class.java)->{
                ActionLaporViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TrainingRecommendationViewModel::class.java)->{
                TrainingRecommendationViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
        }
    }

    companion object{
        private var INSTANCE: ViewModelFactory? = null

        fun clearInstance(){
            UserRepository.clearInstance()
            INSTANCE = null
        }
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}