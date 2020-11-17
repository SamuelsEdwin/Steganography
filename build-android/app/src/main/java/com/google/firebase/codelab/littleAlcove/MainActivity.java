//This program is a modification of the codelab friendly chat tutorial
package com.google.firebase.codelab.littleAlcove;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        //Their message
        TextView theirMessageTextView;


        TextView nameTextView;
        View theirAvatar;

        TextView customMyMessage;



        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            customMyMessage = (TextView) itemView.findViewById(R.id.my_message_body);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);


            theirAvatar = (View) itemView.findViewById(R.id.avatar);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            theirMessageTextView = (TextView) itemView.findViewById(R.id.message_body);




        }
    }

    public interface UserDataStatus{
        void dataIsLoaded(User user);
    }



    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    public static final String USER_CHILD = "users";


    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final int REQUEST_SOUND = 3;

    public static final String ANONYMOUS = "anonymous";

    private String mUsername;
    private String mPhotoUrl;

    private static String SOUND_MIME ="audio/*";
    private static String IMAGE_MIME ="image/*";

    private static String SOUND_DIR = "Sound";
    private static String IMAGE_DIR = "Images";

    private SharedPreferences mSharedPreferences;
    private GoogleSignInClient mSignInClient;
    private static final String AUDIOURL = "https://cdn.pixabay.com/photo/2017/07/09/20/48/speaker-2488096_960_720.png";

    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private ImageView mAddSoundImageView;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<AlcoveMessage, MessageViewHolder>
            mFirebaseAdapter;
    private UserNameUtil userNameUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mUsername = ANONYMOUS;
        userNameUtil = new UserNameUtil();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {

            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }


        ValueEventListener userNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                UserDataStatus dataStatus = new UserDataStatus() {
                    @Override
                    public void dataIsLoaded(User user) {
                        if(user!=null) {
                            mUsername = user.getFullName();
                        }else {
                            try {

                                mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid()).setValue(new User(userNameUtil.getName(),userNameUtil.getUserName()));
                                mUsername=userNameUtil.getUserName();

                            }catch (NullPointerException e) {
                                e.printStackTrace();
                                System.exit(0);
                            }
                        }
                    }
                };

                dataStatus.dataIsLoaded(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
                // [START_EXCLUDE]
                Toast.makeText(MainActivity.this, "Failed to load User.",
                        Toast.LENGTH_SHORT).show();
                try {

                    mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid()).setValue(new User(userNameUtil.getName(),userNameUtil.getUserName()));
                    mUsername=userNameUtil.getUserName();

                }catch (NullPointerException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        };










        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        DatabaseReference databaseReference = mFirebaseDatabaseReference.child(USER_CHILD).child(mFirebaseUser.getUid()).getRef();
        databaseReference.addValueEventListener(userNameListener);






        SnapshotParser<AlcoveMessage> parser = new SnapshotParser<AlcoveMessage>() {
            @Override
            public AlcoveMessage parseSnapshot(DataSnapshot dataSnapshot) {
                AlcoveMessage alcoveMessage = dataSnapshot.getValue(AlcoveMessage.class);
                if (alcoveMessage != null) {
                    alcoveMessage.setId(dataSnapshot.getKey());
                }
                return alcoveMessage;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<AlcoveMessage> options =
                new FirebaseRecyclerOptions.Builder<AlcoveMessage>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<AlcoveMessage, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final  MessageViewHolder viewHolder,
                                            int position,
                                            AlcoveMessage alcoveMessage) {


                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                setTheirVisibility(viewHolder,false);
                setMyVisibility(viewHolder,false);
                setCustomMyVisibility(viewHolder,false);
                if (alcoveMessage.getText() != null) {



                    if(alcoveMessage.getName().equalsIgnoreCase(mUsername)) {
                        viewHolder.customMyMessage.setText(alcoveMessage.getText());


                        setCustomMyVisibility(viewHolder,true);
                    }else {


                        viewHolder.theirMessageTextView.setText(alcoveMessage.getText());
                        viewHolder.nameTextView.setText(alcoveMessage.getName());

                        setTheirVisibility(viewHolder,true);



                    }

                } else if (alcoveMessage.getImageUrl() != null) {
                    String imageUrl = alcoveMessage.getImageUrl();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().toString();
                                            Glide.with(viewHolder.messageImageView.getContext())
                                                    .load(downloadUrl)
                                                    .into(viewHolder.messageImageView);
                                        } else {
                                            Log.w(TAG, "Getting download url was not successful.",
                                                    task.getException());
                                        }
                                    }
                                });
                    } else {
                        Glide.with(viewHolder.messageImageView.getContext())
                                .load(alcoveMessage.getImageUrl())
                                .into(viewHolder.messageImageView);
                    }
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }


                viewHolder.messengerTextView.setText(alcoveMessage.getName());
                if (alcoveMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(MainActivity.this)
                            .load(alcoveMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }

            }
        };


        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlcoveMessage alcoveMessage = new
                        AlcoveMessage(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl,
                        null /* no image */);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                        .push().setValue(alcoveMessage);
                mMessageEditText.setText("");
            }
        });

        mAddMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(IMAGE_MIME);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        mAddSoundImageView = (ImageView) findViewById(R.id.addSoundView);

        mAddSoundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(SOUND_MIME);
                startActivityForResult(intent, REQUEST_SOUND);
            }
        });
    }

    
    private void setTheirVisibility (final  MessageViewHolder viewHolder, boolean visible ) {
        if(visible) {
            viewHolder.theirMessageTextView.setVisibility(TextView.VISIBLE);
            viewHolder.nameTextView.setVisibility(TextView.VISIBLE);
        } else {
            viewHolder.theirMessageTextView.setVisibility(TextView.GONE);
            viewHolder.nameTextView.setVisibility(TextView.GONE);
            viewHolder.theirAvatar.setVisibility(View.GONE);
        }
        viewHolder.messageImageView.setVisibility(ImageView.GONE);

    }

    private void setMyVisibility (final MessageViewHolder viewHolder,boolean visible) {

        if(visible) {

            viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
            viewHolder.messengerTextView.setVisibility(TextView.VISIBLE);
            viewHolder.messengerImageView.setVisibility(ImageView.VISIBLE);

        }else {

            viewHolder.messageTextView.setVisibility(TextView.GONE);
            viewHolder.messengerTextView.setVisibility(TextView.GONE);
            viewHolder.messengerImageView.setVisibility(ImageView.GONE);

        }
        viewHolder.messageImageView.setVisibility(ImageView.GONE);


    }
    private void setCustomMyVisibility(final MessageViewHolder viewHolder,boolean visible) {

        if(visible) {

            viewHolder.customMyMessage.setVisibility(TextView.VISIBLE);


        }else {
            viewHolder.customMyMessage.setVisibility(TextView.GONE);

        }
        viewHolder.messageImageView.setVisibility(ImageView.GONE);


    }
    
    @Override
    public void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            Toast.makeText(MainActivity.this, "User not signed in",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent( MainActivity.this,SignInActivity.class));
            finish();
        }
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                mSignInClient.signOut();

                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE || requestCode ==REQUEST_SOUND) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());
                    AlcoveMessage tempMessage;
                    if(requestCode==REQUEST_SOUND) {
                            tempMessage = new AlcoveMessage(null, mUsername, mPhotoUrl,
                                AUDIOURL,mMessageEditText.getText().toString());
                    }else {
                            tempMessage = new AlcoveMessage(null, mUsername, mPhotoUrl,
                                AUDIOURL);
                    }




                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).push()
                            .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        String key = databaseReference.getKey();
                                        StorageReference storageReference =
                                                FirebaseStorage.getInstance()
                                                        .getReference(mFirebaseUser.getUid())
                                                        .child((requestCode==REQUEST_SOUND)?SOUND_DIR:IMAGE_DIR)
                                                        .child(key)
                                                        .child(uri.getLastPathSegment());


                                        if(requestCode==REQUEST_SOUND) {
                                            putFileInStorage(storageReference,uri,key,SOUND_DIR);
                                        }else {
                                            putImageInStorage(storageReference, uri, key);
                                        }
                                    } else {
                                        Log.w(TAG, "Unable to write message to database.",
                                                databaseError.toException());
                                    }
                                }
                            });
                }
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(MainActivity.this,
                                            new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        AlcoveMessage alcoveMessage =
                                                                new AlcoveMessage(null, mUsername, mPhotoUrl,
                                                                        task.getResult().toString());
                                                        mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key)
                                                                .setValue(alcoveMessage);
                                                    }
                                                }
                                            });
                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

    private void putFileInStorage(StorageReference storageReference, Uri uri, final String key,String dir) {
        storageReference.child(dir).putFile(uri).addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(MainActivity.this,
                                            new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        AlcoveMessage alcoveMessage =
                                                                new AlcoveMessage(null, mUsername, mPhotoUrl,AUDIOURL,
                                                                        task.getResult().toString());
                                                        mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key)
                                                                .setValue(alcoveMessage);
                                                    }
                                                }
                                            });
                        } else {
                            Log.w(TAG, "Sound upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }




}
