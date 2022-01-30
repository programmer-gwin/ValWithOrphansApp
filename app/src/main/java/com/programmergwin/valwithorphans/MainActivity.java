package com.programmergwin.valwithorphans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    DatabaseReference paymentSummaryRef;

    ProgressDialog progressDialog;

    Button btnUpdateRecord;
    TextView educationPercentage,  foodPercentage,  shelterPercentage,  fundPercentage,  totalDonationAmount,  totalDonationTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpFirebaseConfig();
        setViewByID();
        btnUpdateRecord.setOnClickListener(this);
        readFromDB();
    }

    private void setViewByID() {
        btnUpdateRecord = findViewById(R.id.btnUpdateRecord);
        educationPercentage=findViewById(R.id.edtEducationPercentage);
        foodPercentage = findViewById(R.id.edtFoodPercentage);
        shelterPercentage=findViewById(R.id.edtShelterPercentage);
        fundPercentage=findViewById(R.id.edtFundPercentage);
        totalDonationAmount=findViewById(R.id.edtDonationReceived);
        totalDonationTarget=findViewById(R.id.edtDonationTarget);
    }

    private void setUpFirebaseConfig() {
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        paymentSummaryRef = database.getReference("PAYMENT_SUMMARY");
    }

    @Override
    public void onClick(View view) {
        PerformPaymentSummaryUpdate();
    }

    private void PerformPaymentSummaryUpdate() {
        if(TextUtils.isEmpty(educationPercentage.getText()) || TextUtils.isEmpty(foodPercentage.getText())
                || TextUtils.isEmpty(shelterPercentage.getText()) || TextUtils.isEmpty(fundPercentage.getText())
                || TextUtils.isEmpty(totalDonationAmount.getText())|| TextUtils.isEmpty(totalDonationTarget.getText()))
            Toast.makeText(this, "One or more text is empty.", Toast.LENGTH_SHORT).show();
        else {
            PaymentSummaryModel paymentSummaryModel = new PaymentSummaryModel(educationPercentage.getText().toString(),
                    foodPercentage.getText().toString(), shelterPercentage.getText().toString(), fundPercentage.getText().toString(),
                    totalDonationAmount.getText().toString(), totalDonationTarget.getText().toString());
            WriteToPaymentSummaryDB(paymentSummaryModel);
        }
    }

    private void WriteToPaymentSummaryDB(PaymentSummaryModel paymentSummaryModel){
        try {
            showProgressDialog(true);
            paymentSummaryRef.setValue(paymentSummaryModel);
            showProgressDialog(false);
            showDialog("Success", "Payment Summary Updated Successfully");
        }catch (Exception e){
            e.printStackTrace();
            showDialog("Error", e.getMessage());
        }
    }

    void showDialog(String Title, String Message){
        runOnUiThread(() -> {
            if (!isFinishing()){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(Title).setMessage(Message).setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> readFromDB()).show();
            }
        });
    }

    void showProgressDialog(boolean status){
        if(status)
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", status);
        else
            progressDialog.cancel();
    }

    void readFromDB(){
        showProgressDialog(true);
        // Read from the database
        paymentSummaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try{
                    PaymentSummaryModel value = dataSnapshot.getValue(PaymentSummaryModel.class);
                    Log.d("TAG", "Value is: " + value);
                    if (value != null) {
                        totalDonationAmount.setText(value.TotalDonationAmount.replace(",", ""));
                        totalDonationTarget.setText(value.TotalDonationTarget.replace(",", ""));
                        fundPercentage.setText(value.FundPercentage);
                        foodPercentage.setText(value.FoodPercentage);
                        shelterPercentage.setText(value.ShelterPercentage);
                        educationPercentage.setText(value.EducationPercentage);
                    }else{
                        Log.d("TAG", "Failed to deserialize value.");
                    }
                    showProgressDialog(false);
                }catch (Exception ex){
                    Log.d("TAG", "Failed to read value."+ ex.getMessage());
                    showProgressDialog(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
                showProgressDialog(false);
            }
        });
    }

    /*private void addInitialDataToFirebase() {
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        journalCloudEndPoint = mDatabase.child("journalentris");
        journalCloudEndPoint.setValue("Hello World");

        List<JournalEntry> sampleJournalEntries =
                SampleData.getSampleJournalEntries();
        for (JournalEntry journalEntry: sampleJournalEntries){
            String key = journalCloudEndPoint.push().getKey();
            journalEntry.setJournalId(key);
            journalCloudEndPoint.child(key).setValue(journalEntry);
        }

        List<String> tagNames = SampleData.getSampleTags();
        for (String name: tagNames){
            String tagKey = tagCloudEndPoint.push().getKey();
            Tag tag = new Tag();
            tag.setTagName(name);
            tag.setTagId(tagKey);
            tagCloudEndPoint.child(tag.getTagId()).setValue(category);
        }

    }*/

}