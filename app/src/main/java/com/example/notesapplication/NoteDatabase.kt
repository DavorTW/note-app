package com.example.notesapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile private var instance: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                NoteDatabase::class.java, "note_database")
                .build()
    }
}
