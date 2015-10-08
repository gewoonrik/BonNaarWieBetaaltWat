package riknijessen.nl.bonwiebetaaltwat;

import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rik on 07/10/15.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CameraActivity extends Activity
{

    @Bind(R.id.photo) public ImageView imageview;
    protected String _path;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN	= "photo_taken";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        addLanguageData();
        startCameraActivity();
    }

    public void addLanguageData()   {
        try {
            InputStream input = getAssets().open("nld.traineddata");
            File trainingsDataDirectory = new File(Environment.getExternalStorageDirectory(), "tessdata/");
            trainingsDataDirectory.mkdirs();
            File file = new File(trainingsDataDirectory, "nld.traineddata");
            file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=input.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        _path = image.getAbsolutePath();
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public class ButtonClickHandler implements View.OnClickListener
    {
        public void onClick( View view ){
            Log.i("MakeMachine", "ButtonClickHandler.onClick()" );
            startCameraActivity();
        }
    }

    protected void startCameraActivity()
    {
        Log.i("MakeMachine", "startCameraActivity()" );
        File file = null;
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputFileUri = Uri.fromFile( file );

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult( intent, 0 );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        Log.i( "MakeMachine", "resultCode: " + resultCode );
        switch( resultCode )
        {
            case 0:
                Log.i( "MakeMachine", "User cancelled" );
                break;

            case -1:
                onPhotoTaken();
                break;
        }
    }

    protected void onPhotoTaken()
    {
        Log.i( "MakeMachine", "onPhotoTaken" );

        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        int rotate = 0;

        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
        }

        if (rotate != 0) {
           bitmap = ImagePreprocessor.rotate(bitmap, rotate);
        }
        //make bitmap mutable
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);


        ImagePreprocessor.preProcess(bitmap);
        bitmap = ImagePreprocessor.RuiHuaBitmap(bitmap);
        imageview.setImageBitmap(bitmap);
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(Environment.getExternalStorageDirectory().getAbsolutePath(), "nld");
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789,");
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();
        System.out.println(recognizedText);
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState){
        Log.i( "MakeMachine", "onRestoreInstanceState()");
        if( savedInstanceState.getBoolean( CameraActivity.PHOTO_TAKEN ) ) {
            onPhotoTaken();
        }
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        outState.putBoolean( CameraActivity.PHOTO_TAKEN, _taken );
    }
}