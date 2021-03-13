package com.example.crisol_Jaen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crisol_Jaen.Validation.Validation;
import com.example.crisol_Jaen.culqi.Card;
import com.example.crisol_Jaen.culqi.Token;
import com.example.crisol_Jaen.culqi.TokenCallback;
import com.example.crisol_Jaen.model.Book;
import com.example.crisol_Jaen.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Book book = new Book();
    private Order pedido = new Order();

    private Validation validation = new Validation();
    private ProgressDialog progress;
    private TextInputEditText etCardnumber, etCvv, etMonth, etYear;
    private TextView tvKind_card;

    private static final String TAG = "PayActivity";
    private String email, direccion, telefono;
    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        progress = new ProgressDialog(this);
        ImageView ivImage = findViewById(R.id.ivImage);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        final EditText etDireccion = findViewById(R.id.EtDireccionPay);
        final EditText etTelefono = findViewById(R.id.EtTelefonoPay);

        final TextView tvUserName = findViewById(R.id.userName);
        final TextView tvUserEmail = findViewById(R.id.userEmail);

        etCardnumber = findViewById(R.id.etCardNumber);
        etCvv = findViewById(R.id.etCardCVV);
        etMonth = findViewById(R.id.etCardExpMonth);
        etYear = findViewById(R.id.etCardExpYear);
        tvKind_card = findViewById(R.id.tvKindCard);

        progress.setMessage("Validando información de la tarjeta");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        etCvv.setEnabled(false);

        // Toolbar
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        db.collection("user")
                .whereEqualTo("id", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // user
                                email = document.getString("email");
                                tvUserName.setText(document.getString("name")+ " " + document.getString("lastname"));
                                tvUserEmail.setText(email);
                            }
                        } else {
                            //
                        }
                    }
                });

        //Validation input Cards
        //TODO Validar cada input (ChangedListener, AfterChanges, revisar KindCard (linea 84)
        etCardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    etCvv.setEnabled(true); }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etCardnumber.getText().toString();
                if (s.length() == 0) {
                    etCardnumber.setBackgroundResource(R.drawable.border_error);
                }

                if (validation.luhn(text)) {
                    etCardnumber.setBackgroundResource(R.drawable.border_sucess);
                } else {
                    etCardnumber.setBackgroundResource(R.drawable.border_error);
                }

                int cvv = validation.bin(text, tvKind_card);
                if(cvv > 0){
                    etCvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cvv)});
                    etCvv.setEnabled(true);
                } else {
                    etCvv.setEnabled(false);
                    etCvv.setText("");
                }
            }
        });

        etYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {  }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etYear.getText().toString();
                if(validation.year(text)){
                    etYear.setBackgroundResource(R.drawable.border_error);
                } else {
                    etYear.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        etMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etMonth.getText().toString();
                if(validation.month(text)){
                    etMonth.setBackgroundResource(R.drawable.border_error);
                } else {
                    etMonth.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        if(getIntent().hasExtra("book")){
            book = getIntent().getParcelableExtra("book");
            // Utilizando la librería Picasso
            Picasso.get().load(book.getImage()).into(ivImage);
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthorId());
            // btnPay
            Button btnPay = findViewById(R.id.btnPay);
            btnPay.setText("Pagar " + book.getPrice());
            btnPay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    progress.show();
                    tokenCard();
                    direccion = etDireccion.getText().toString().trim();
                    telefono = etTelefono.getText().toString().trim();
                    Intent intConfirm = new Intent(PayActivity.this, ConfirmActivity.class);
                    intConfirm.putExtra("book", book);

                    //generarPedido(book, direccion, telefono);
                    startActivity(intConfirm);
                }
            });
        }
    }

    private void tokenCard(){
        //TODO Tokenizar tarjeta
        int month = Integer.parseInt(etMonth.getText().toString());
        int year = Integer.parseInt(etYear.getText().toString());
        Card card = new Card(etCardnumber.getText().toString(), etCvv.getText().toString(),email, month, year);

        Token token = new Token("pk_test_80ee6b70d9617223");

        token.createToken(getApplicationContext(), card, new TokenCallback() {
            @Override
            public void onSuccess(JSONObject token) {
                try{
                    idToken = token.get("id").toString();
                    Log.d(TAG, idToken);
                } catch (Exception ex){
                    progress.hide();
                }
                progress.hide();
                generarPedido(book, direccion, telefono);
            }
            @Override
            public void onError(Exception error) { progress.hide(); }
        });
    }

    private void generarCargo(){
        //TODO Conectarse al backend y enviar el idtoken, monto, tipo moneda y correo
//        generarPedido(book, direccion, telefono);
    }

    private void generarPedido(Book book, String direccion, String telefono){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Date currentTime = Calendar.getInstance().getTime();

        final Map<String, Object> order = new HashMap<>();
        order.put("costoPedido", book.getPrice());
        order.put("costoTotal", (book.getPrice() + 5.0));
        order.put("direccion", direccion);
        order.put("estado", "Pendiente");
        order.put("fecha", currentTime);
        order.put("productos", book.getTitle());
        order.put("idUsuario", currentUser.getUid());
        order.put("telefono", telefono);
        order.put("token", idToken);
            db.collection("order")
                    .add(order)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot generado con ID: " + documentReference.getId());
                            DocumentReference pedidoRef = db.collection("order").document(documentReference.getId());

                            pedidoRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot ds) {
                                    if(ds.exists()){
                                        Order nuevopedido = new Order(ds.getId(),
                                                ds.getString("direccion"),
                                                ds.getString("idUsuario"),
                                                ds.getString("telefono"),
                                                ds.getString("token"),
                                                ds.getString("estado"),
                                                ds.getDouble("costoPedido"),
                                                ds.getDouble("costoTotal"),
                                                ds.getString("productos"));
                                        Log.d(TAG, "Pedido creado como " + nuevopedido);
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error al generar documento", e);
                        }
                    });
    }
}
