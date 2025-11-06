package com.example.movieticketapp.Activity.Ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketapp.Activity.HomeActivity;
import com.example.movieticketapp.Activity.Notification.NotificationActivity;
import com.example.movieticketapp.Activity.Report.ReportActivity;
import com.example.movieticketapp.Activity.Wallet.MyWalletActivity;
import com.example.movieticketapp.Model.Ticket;
import com.example.movieticketapp.Model.Users;
import com.example.movieticketapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyTicketAllActivity extends AppCompatActivity {

    private ListView listView;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private final ArrayList<Ticket> arrayList = new ArrayList<>();
    private TicketListAdapter adapter;

    private BottomNavigationView bottomNavigationView;
    private Button btnAll, btnNew, btnExpired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ticket_all_screen);

        initViews();
        setupBottomNavigation();
        setupButtonListeners();

        // Load mặc định tất cả vé
        setButtonSelected(btnAll);
        loadListTicket("all");
    }

    private void initViews() {
        listView = findViewById(R.id.ListViewTicket);
        btnAll = findViewById(R.id.buttonAllTicket);
        btnNew = findViewById(R.id.buttonNewsTicket);
        btnExpired = findViewById(R.id.buttonExpiredTicket);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupButtonListeners() {
        btnAll.setOnClickListener(v -> {
            setButtonSelected(btnAll);
            loadListTicket("all");
        });

        btnNew.setOnClickListener(v -> {
            setButtonSelected(btnNew);
            loadListTicket("new");
        });

        btnExpired.setOnClickListener(v -> {
            setButtonSelected(btnExpired);
            loadListTicket("expired");
        });

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Ticket ticket = (Ticket) adapterView.getItemAtPosition(position);
            if (ticket == null) return;

            Intent intent = new Intent(MyTicketAllActivity.this, TicketDetailActivity.class);
            intent.putExtra("ticket", ticket);
            startActivity(intent);
        });
    }

    private void setButtonSelected(Button selected) {
        // Reset tất cả nút
        btnAll.setText(null);
        btnNew.setText(null);
        btnExpired.setText(null);
        btnAll.setSelected(false);
        btnNew.setSelected(false);
        btnExpired.setSelected(false);

        // Đặt lại nút được chọn
        selected.setSelected(true);
        switch (selected.getId()) {
            case R.id.buttonAllTicket:
                btnAll.setText("All");
                break;
            case R.id.buttonNewsTicket:
                btnNew.setText("New");
                break;
            case R.id.buttonExpiredTicket:
                btnExpired.setText("Expired");
                break;
        }

        arrayList.clear();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.getMenu().findItem(R.id.ticketPage).setChecked(true);

        try {
            if (Users.currentUser != null && "admin".equals(Users.currentUser.getAccountType())) {
                Menu menu = bottomNavigationView.getMenu();
                menu.findItem(R.id.walletPage).setVisible(false);
                menu.findItem(R.id.ReportPage).setVisible(true);
            }
        } catch (Exception e) {
            Log.e("MyTicketAllActivity", "User role check failed", e);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent = null;
            int id = item.getItemId();

            if (id == R.id.homePage) {
                intent = new Intent(this, HomeActivity.class);
            } else if (id == R.id.walletPage) {
                intent = new Intent(this, MyWalletActivity.class);
            } else if (id == R.id.ReportPage) {
                intent = new Intent(this, ReportActivity.class);
            } else if (id == R.id.NotificationPage) {
                intent = new Intent(this, NotificationActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void loadListTicket(String type) {
        firestore.collection("Ticket")
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("FirestoreError", e.getMessage());
                            return;
                        }

                        if (snapshots == null || snapshots.isEmpty()) {
                            arrayList.clear();
                            if (adapter != null) adapter.notifyDataSetChanged();
                            return;
                        }

                        List<DocumentSnapshot> docs = snapshots.getDocuments();
                        Calendar calendar = Calendar.getInstance();
                        Date now = calendar.getTime();

                        arrayList.clear();

                        for (DocumentSnapshot doc : docs) {
                            if (!doc.exists()) continue;
                            if (!doc.contains("userID") || !doc.contains("time")) continue;

                            String userID = doc.getString("userID");
                            if (mAuth.getUid() == null || !mAuth.getUid().equals(userID)) continue;

                            Ticket ticket = doc.toObject(Ticket.class);
                            if (ticket == null) continue;

                            Date showTime = doc.getTimestamp("time").toDate();
                            if (showTime == null) continue;

                            switch (type) {
                                case "new":
                                    if (now.before(showTime)) arrayList.add(ticket);
                                    break;
                                case "expired":
                                    if (now.after(showTime) || now.equals(showTime)) arrayList.add(ticket);
                                    break;
                                default:
                                    arrayList.add(ticket);
                                    break;
                            }
                        }

                        if (adapter == null) {
                            adapter = new TicketListAdapter(MyTicketAllActivity.this,
                                    R.layout.list_ticket_view, arrayList);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
