package khoiviet24.recfit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileImage extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 10;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    protected ImageView mImageView;
    protected RoundImage mRoundedImage;
    protected Button mTakePhoto;
    protected Button mOpenGallery;
    protected String mImageFileLocation;
    protected LinearLayout mLayout;
    //protected TextView mDone;
    protected ImageView mButtonBox;

    protected byte[] scaledData;
    protected ParseFile photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_image);
        //getSupportActionBar().hide();

        mImageView = (ImageView)findViewById(R.id.profile_picture_imageview);
        mTakePhoto = (Button)findViewById(R.id.take_photo_btn);
        mOpenGallery = (Button)findViewById(R.id.camera_roll_btn);
       //mDone = (TextView)findViewById(R.id.done_txt);
        mButtonBox = (ImageView)findViewById(R.id.button_box_img);

        if(ParseUser.getCurrentUser().getParseFile("Picture") != null){
            ParseFile profileImage = (ParseFile)ParseUser.getCurrentUser().getParseFile("Picture");
            profileImage.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if(e == null){
                        Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        mRoundedImage = new RoundImage(cropToSquare(bMap));
                        mImageView.setImageDrawable(mRoundedImage);
                    }
                    else{

                    }
                }
            });
        }
        /*
        mLayout = (LinearLayout)findViewById(R.id.edit_profile_picture_layout);
        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                String mStoredImage = ParseUser.getCurrentUser().get("profileImage").toString();
                if (mStoredImage != "") {
                    mImageFileLocation = mStoredImage;
                    File imgFile = new File(mImageFileLocation);
                    if (imgFile.exists()) {
                        //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        rotateImage(setReducedImageSize());
                    }
                }
            }
        });
        */

        mButtonBox.setImageDrawable(getResources().getDrawable(R.drawable.button_box));

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CAMERA_REQUEST); //unique identifier

            }
        });

        mOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                //invoke the imgage gallery using an implicit intent
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                //where to get the data
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();

                //URI representation
                Uri data = Uri.parse(pictureDirectoryPath);

                //set the data and type. Get all image types
                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), IMAGE_GALLERY_REQUEST);
            }
        });

        /*
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditPicToHome = new Intent(EditProfileImage.this, NavDrawer.class);
                startActivity(EditPicToHome);
            }
        });
*/

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            //mImageView.setImageBitmap(photoCapturedBitmap);
            rotateImage(setReducedImageSize());

            ParseUser mCurrentUser = ParseUser.getCurrentUser();
            //mCurrentUser.put("profileImage", mImageFileLocation.trim());
            photoFile = new ParseFile(mCurrentUser.getUsername()+ "_profile_photo.jpg", scaledData);
            mCurrentUser.put("Picture", photoFile);
            mCurrentUser.saveInBackground();

            //Bitmap cameraImage =(Bitmap) data.getExtras().get("data");
            //mImageView.setImageBitmap(cameraImage);

            //mRoundedImage = new RoundImage(cameraImage);
            //mImageView.setImageDrawable(mRoundedImage);
        }

        if(requestCode == IMAGE_GALLERY_REQUEST && resultCode == Activity.RESULT_OK){
            //mImageView.setImageURI(data.getData());

            Uri imageUri = data.getData();
            //File imageFile = new File(getRealPathFromURI(imageUri));

            //declare a stream to read image data
            InputStream inputStream;

            try {
                inputStream = this.getContentResolver().openInputStream(imageUri);

                //Bitmap image = BitmapFactory.decodeStream(inputStream);

                //mImageView.setImageBitmap(image);
                //rotateImage(image);
                mImageFileLocation = getRealPathFromURI(imageUri);
                rotateImage(setReducedImageSize());

                ParseUser mCurrentUser = ParseUser.getCurrentUser();
                //mCurrentUser.put("profileImage", mImageFileLocation.trim());
                photoFile = new ParseFile(mCurrentUser.getUsername()+ "_profile_photo.jpg", scaledData);
                mCurrentUser.put("Picture", photoFile);
                mCurrentUser.saveInBackground();
                Toast.makeText(this, "Photo Saved", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }
        }
    }

    File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }

    private Bitmap setReducedImageSize(){
        int targetImageViewWidth = mImageView.getWidth();
        int targetImageViewHeight = mImageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        //Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        //mImageView.setImageBitmap(photoReducedSizeBitmap);

        return BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
    }

    private void rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch(orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
            default:
        }

        //create rotate, scaled down image
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        //Bitmap profileImageScaled = Bitmap.createScaledBitmap(bitmap, 200, 200 * bitmap.getHeight() / bitmap.getWidth(), false);
        //Matrix profileMatrix = new Matrix();
        //profileMatrix.postRotate(90);
        //Bitmap rotatedScaledImage = Bitmap.createBitmap(profileImageScaled, 0, 0, profileImageScaled.getWidth(), profileImageScaled.getHeight(), matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        scaledData = bos.toByteArray();

        mRoundedImage = new RoundImage(cropToSquare(rotatedBitmap));
        mImageView.setImageDrawable(mRoundedImage);

        /*
        mRoundedImage = new RoundImage(cropToSquare(rotatedBitmap));
        mImageView.setImageDrawable(mRoundedImage);*/


        //mImageView.setImageBitmap(cropToSquare(rotatedBitmap));

    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

}
