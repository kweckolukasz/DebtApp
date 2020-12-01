package Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import Room.DebtSet;

@Dao
public interface DebtSetDao {

    @Insert
    void insert(DebtSet debtSet);
    @Delete
    void delete(DebtSet debtSet);
    @Update
    void update(DebtSet debtSet);

// To musisz robić w viewModel, to miejsce jest tylko do wyciągnięcia danych
    //Pobierz:
        //ZGrupy i:
            //Aktywne:
                //Unresolved
                //Resolving
                //Resolved
            //NieAktywne:
                //Unresolved
                //Resolving
                //Resolved
        //PozaGrupą
             //Aktywne:
                //Unresolved
                //Resolving
                //Resolved
            //NieAktywne:
                //Unresolved
                //Resolving
                //Resolved
}
