package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private View onlineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;
    private MessageAdapter messageAdapter;
    private String currentUserId;
    private String otherUserId;
    private ChatViewModelFactory chatViewModelFactory;
    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";

    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        messageAdapter = new MessageAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);
        chatViewModelFactory= new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider (this, chatViewModelFactory).get(ChatViewModel.class);
        observeViewModel();
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message= new Message(editTextMessage.getText().toString().trim(), currentUserId, otherUserId);
                viewModel.sendMessage(message);
            }
        });


    }

    private void observeViewModel(){
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessages(messages);
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error!=null){
                    Toast.makeText(ChatActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent){
                    editTextMessage.setText("");
                }
            }
        });
        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s", user.getName(), user.getLastName());
                textViewTitle.setText(userInfo);
                int backgroundResId;
                if (user.isOnline()){
                    backgroundResId = R.drawable.circle_green;
                }else{
                    backgroundResId = R.drawable.circle_red;
                }
                Drawable background = ContextCompat.getDrawable(ChatActivity.this, backgroundResId);
                onlineStatus.setBackground(background);
            }
        });
    }

    private void initViews(){
        textViewTitle= findViewById(R.id.textViewTitle);
        onlineStatus= findViewById(R.id.viewOnlineStatus);
        recyclerViewMessages= findViewById(R.id.recyclerViewMessages);
        editTextMessage= findViewById(R.id.editTextMessage);
        imageViewSendMessage= findViewById(R.id.imageViewSendMessage);
    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }
}