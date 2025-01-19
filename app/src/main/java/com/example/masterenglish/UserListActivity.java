package com.example.masterenglish;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ListView userList = findViewById(R.id.user_list);
        Button btnDelete = findViewById(R.id.btn_delete_user);

        databaseHelper = new DatabaseHelper(this);

        try {
            Cursor cursor = databaseHelper.getAllUsers();
            if (cursor != null && cursor.getCount() > 0) {
                String[] fromColumns = { "email", "login" };
                int[] toViews = { R.id.user_email, R.id.user_login };


                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this,
                        R.layout.user_item,
                        cursor,
                        fromColumns,
                        toViews,
                        0
                );

                userList.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Нет данных для отображения", Toast.LENGTH_SHORT).show();
            }

            btnDelete.setOnClickListener(v -> {
                boolean isDeleted = databaseHelper.deleteUser("example_login");
                if (isDeleted) {
                    Toast.makeText(this, "Пользователь удалён", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Ошибка удаления пользователя", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка загрузки данных: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        userList.setOnItemClickListener((parent, view, position, id) -> {
            Cursor selectedUserCursor = (Cursor) parent.getItemAtPosition(position);
            String selectedLogin = selectedUserCursor.getString(selectedUserCursor.getColumnIndexOrThrow("login"));

            btnDelete.setOnClickListener(v -> {
                boolean isDeleted = databaseHelper.deleteUser(selectedLogin);
                if (isDeleted) {
                    Toast.makeText(this, "Пользователь " + selectedLogin + " удалён", Toast.LENGTH_SHORT).show();
                    Cursor updatedCursor = databaseHelper.getAllUsers();
                    ((SimpleCursorAdapter) userList.getAdapter()).changeCursor(updatedCursor);
                } else {
                    Toast.makeText(this, "Ошибка удаления пользователя", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
