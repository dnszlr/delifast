package mobilecomputing.delifast.interaction.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import android.provider.MediaStore;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import io.grpc.Context;
import mobilecomputing.delifast.R;
import mobilecomputing.delifast.interaction.order.OrderViewModel;

public class ProfileUserDataFragment extends Fragment {

    private ProfileViewModel viewModel;

    private CardView cardProfileUserData;
    private ConstraintLayout layoutProfileUserdataTransation;
    private ImageView profilePic;
    private TextView tvUserName, tvCountOrders, tvCountDeliveries, tvCountRatings;
    private EditText etUserName;
    private ImageButton editUserName;

    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public ProfileUserDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileUserDataView = inflater.inflate(R.layout.fragment_profile_user_data, container, false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initView(profileUserDataView);

        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        viewModel.getUserById(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), user -> {
            tvUserName.setText(user.getName());
            etUserName.setText(user.getName());
        });

        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null){
                tvCountOrders.setText(String.valueOf(orders.size()));
            }
        });
        viewModel.getAllByCustomerId(FirebaseAuth.getInstance().getUid());



        return profileUserDataView;
    }

    private void initView(View view) {
        cardProfileUserData = view.findViewById(R.id.cardProfileUserData);
        layoutProfileUserdataTransation = view.findViewById(R.id.layoutProfileUserdataTransation);

        profilePic = view.findViewById(R.id.imgProfilePic);
        downloadPicture(FirebaseAuth.getInstance().getUid());
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        tvUserName = view.findViewById(R.id.tvProfileUserName);
        etUserName = view.findViewById(R.id.tvProfileUserDataUserName);
        editUserName = view.findViewById(R.id.ibEditUserName);

        tvCountOrders = view.findViewById(R.id.tvProfileUserDataCountOrders);
        tvCountDeliveries = view.findViewById(R.id.tvProfileUserDataCountDeliveries);
        tvCountRatings = view.findViewById(R.id.tvProfileUserDataCountRatings);
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 221);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 221 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();

        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Bild wird hochgeladen...");
        pd.show();

        String imgID = FirebaseAuth.getInstance().getUid();
        StorageReference ref = storageReference.child("images/" + imgID);

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Das Bild wurde erfolgreich geladen.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double processPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) processPercent + "%");
            }
        });
    }

    private void downloadPicture(String uId) {

        storageReference.child("images/" + uId).getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePic.setImageBitmap(bmp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getContext(), "Das Bild wurde nicht geladen.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}