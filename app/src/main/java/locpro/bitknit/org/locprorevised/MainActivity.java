package locpro.bitknit.org.locprorevised;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.NetworkOnMainThreadException;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.ConnectException;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    private ImageView bookmark;
    private TextView details;
    private Toolbar toolbar;
    public DatabaseReference dataBase;
    private ImageView thumbnail;
    private ImageView titleImage;
    private TextView title;
    private TextView author;
    private  TextView description;
    private TextView price;
    private TextView numBids;
    private TextView daysLeft;
    private TextView topBid;
    private TextView topBidder;
    private TextView titleName;
    public Integer tpBd;
    public Integer bidnumber;
    public String tpBddr;
    public String FromSush;
    public Integer dummnyVariable;
    public Integer initialValue;
    public Integer viewnumber;
    public int n=1;

    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FromSush ="266e29bb17c978cMarameriurmila";

        TextView title = (TextView)findViewById(R.id.title);
        TextView writer = (TextView)findViewById(R.id.writer);
        ImageView thumbnail = (ImageView)findViewById(R.id.thumbnail);
        ImageView titleImage = (ImageView) findViewById(R.id.titleImage);


        final StorageReference msref = FirebaseStorage.getInstance().getReference().child(FromSush);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dataBase = FirebaseDatabase.getInstance().getReference();


        myDialog = new Dialog(this);

        Glide.with(MainActivity.this).using(new FirebaseImageLoader()).load(msref).into(titleImage);
        Glide.with(MainActivity.this).using(new FirebaseImageLoader()).load(msref).into(thumbnail);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Astrophysics for Dummies");
        }


        bookmark = (ImageView)findViewById(R.id.bookmark);
        TextView tx = (TextView)findViewById(R.id.description);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Roboto-Regular.ttf");
        Typeface cm_font = Typeface.createFromAsset(getAssets(),  "fonts/Roboto-Thin.ttf");
        tx.setTypeface(custom_font);
        title.setTypeface(custom_font);
        writer.setTypeface(custom_font);


        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView topBid = (TextView)findViewById(R.id.topBid);
                TextView topBidder = (TextView)findViewById(R.id.topBidder) ;
                TextView description = (TextView)findViewById(R.id.description);
                TextView price = (TextView)findViewById(R.id.price);
                TextView numBids = (TextView)findViewById(R.id.bidNumber);
                TextView daysLeft = (TextView)findViewById(R.id.daysLeft);
                TextView writer = (TextView)findViewById(R.id.writer);
                TextView title = (TextView)findViewById(R.id.title);
                TextView titleName = (TextView)findViewById(R.id.titleName);
                TextView views = (TextView)findViewById(R.id.views);
                String andId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


                String author = dataSnapshot.child("book_database").child(FromSush).child("author").getValue(String.class);
                writer.setText(author);
                if(dataSnapshot.child("bidding_database").child(FromSush).child("views").getValue()!=null) {
                    while (n < 2) {
                        viewnumber = dataSnapshot.child("bidding_database").child(FromSush).child("views").getValue(Integer.class);
                        viewnumber = viewnumber + 1;
                        dataBase.child("bidding_database").child(FromSush).child("views").setValue(viewnumber);
                        views.setText(viewnumber.toString());
                        n++;
                    }
                }
                else{
                    dataBase.child("bidding_database").child(FromSush).child("views").setValue(0);
                }
                String tt = dataSnapshot.child("book_database").child(FromSush).child("title").getValue(String.class);
                title.setText(tt);
                titleName.setText(tt);

                String des = dataSnapshot.child("book_database").child(FromSush).child("description").getValue(String.class);
                description.setText(des);

                String prc = dataSnapshot.child("book_database").child(FromSush).child("price").getValue(String.class);
                price.setText("Rs." + prc);

                bidnumber = dataSnapshot.child("bidding_database").child(FromSush).child("num_bids").getValue(Integer.class);
                numBids.setText(bidnumber.toString());
                String fullDate = dataSnapshot.child("bidding_database").child("89801db7c45f5c84meandurmi").child("created_timestamp").getValue(String.class);
                Long b = Long.valueOf(fullDate)+ 604800000L;
                Date currDate = new Date();
                Long a = currDate.getTime();
                Long d = b-a;
                d = d/86400000;
                Integer e = (int) (long) d;
//                604800000
                daysLeft.setText(e.toString());

                tpBddr = dataSnapshot.child("bidding_database").child(FromSush).child("max_bid").child("user").getValue(String.class);

                if(dataSnapshot.child("bidding_database").child(FromSush).child("bids").child(andId).getValue()!=null)
                {
                    initialValue = dataSnapshot.child("bidding_database").child(FromSush).child("bids").child(andId).child("price").getValue(Integer.class);
                    dummnyVariable = 0;
                }
                else {
                    dummnyVariable =1;
                }
                        String tB = dataSnapshot.child("user_database").child(tpBddr).child("display_name").getValue(String.class);
                topBidder.setText(tB);

                tpBd = dataSnapshot.child("bidding_database").child(FromSush).child("max_bid").child("value").getValue(Integer.class);
                topBid.setText("Rs." + tpBd.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void change(View v){
        ImageView abc = (ImageView)v;
        if (abc.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.bookmark).getConstantState())
        {
            abc.setImageResource(R.drawable.bookmark1);
        }
        else
        {
            abc.setImageResource(R.drawable.bookmark);
        }

    }
    public void change1(View v){
        ImageView abc = (ImageView)v;
        if (abc.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.wishlist).getConstantState())
        {
            abc.setImageResource(R.drawable.wishlist1);
        }
        else
        {
            abc.setImageResource(R.drawable.wishlist);
        }

    }
    public void popUp(View v) {
        try{
        Button txtclose;
        Button placeBid;
        TextView mxBd;
        String bdAmnt;
        Integer bdmt;

        myDialog.setContentView(R.layout.popupwindow);
        txtclose =(Button) myDialog.findViewById(R.id.txtclose);
        placeBid = (Button) myDialog.findViewById(R.id.placeBid);
        mxBd = (TextView) myDialog.findViewById(R.id.maxBid);

        mxBd.setText("Top Bid Amount is Rs." + tpBd.toString());

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                Date dt = new Date();
                Long dat = dt.getTime();
                EditText bidAmount = (EditText) myDialog.findViewById(R.id.bidAmount);
                String bidAmnt = String.valueOf(bidAmount.getText());
                Integer bdmt = Integer.valueOf(bidAmnt);
                String andId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                if(bdmt>9999)
                {

                    Toast.makeText(MainActivity.this,"Amount must be less than 10,000",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(dummnyVariable==0){
                        if(bdmt<=initialValue)
                        {
                            Toast.makeText(MainActivity.this,"Your Bid Amount must be more than your last Bid amount",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            myDialog.dismiss();
                            dataBase.child("bidding_database").child(FromSush).child("num_bids").setValue(bidnumber+1);

                            dataBase.child("bidding_database").child(FromSush).child("bids").child(andId).child("price").setValue(bdmt);
                            dataBase.child("bidding_database").child(FromSush).child("bids").child(andId).child("time_stamp").setValue(dat.toString());
                            if(bdmt>tpBd){
                                dataBase.child("bidding_database").child(FromSush).child("max_bid").child("user").setValue(andId);
                                dataBase.child("bidding_database").child(FromSush).child("max_bid").child("value").setValue(bdmt);

                                Toast.makeText(MainActivity.this,"You are the top bidder now...",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this,"Your Bid amount is not the top Bid...",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else {
                        myDialog.dismiss();
                        dataBase.child("bidding_database").child(FromSush).child("num_bids").setValue(bidnumber+1);

                        dataBase.child("bidding_database").child(FromSush).child("bids").child(andId).child("price").setValue(bdmt);
                        dataBase.child("bidding_database").child(FromSush).child("bids").child(andId).child("time_stamp").setValue(dat.toString());
                        if(bdmt>tpBd){
                            dataBase.child("bidding_database").child(FromSush).child("max_bid").child("user").setValue(andId);
                            dataBase.child("bidding_database").child(FromSush).child("max_bid").child("value").setValue(bdmt);

                            Toast.makeText(MainActivity.this,"You are the top bidder now...",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Your Bid amount is not the top Bid...",Toast.LENGTH_LONG).show();
                        }
                    }
}}
                catch (Exception e1){
                    Toast.makeText(MainActivity.this,"Amount must be filled",Toast.LENGTH_SHORT).show();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }catch (NullPointerException e2 ){
            Toast.makeText(MainActivity.this,"No internet Connection",Toast.LENGTH_SHORT).show();
        }
    }
}


