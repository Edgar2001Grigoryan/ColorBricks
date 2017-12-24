package ru.startandroid.gridview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridView gvMain;
    CustomAdapter adapter;
    private int columnsCount =10;
    final String FILENAME = "file.dat";
    EditText etChangeSize;
    TextView tvScore;
    ArrayList<ColorButtonModel> colorsArray = new ArrayList<>();
    int buttonCount;
    Random rand = new Random();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etChangeSize = (EditText)findViewById(R.id.etChangeSize);
        gvMain = (GridView) findViewById(R.id.gvmain);
        tvScore = (TextView) findViewById(R.id.tvScore);
        handleChangeSize();
        initializeColorsArray();
        gvMain.setNumColumns(columnsCount);
        adapter=new CustomAdapter(this, colorsArray, myClickListener);
        gvMain.setAdapter(adapter);
    }

    public void onclickChangeSize(){
     handleChangeSize();
    }

    private void handleChangeSize() {
        if ((etChangeSize.getText().length() == 0)) {
            columnsCount = 10;
        }
        else {
            columnsCount = Integer.parseInt(etChangeSize.getText().toString());
        }
    }

    public void onclickSave() {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < columnsCount*columnsCount; i++){
            ColorButtonModel model = colorsArray.get(i);
            array.add(model.getColorResId());
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(FILENAME);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onclickLoad() {
        ArrayList<Integer> array = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(FILENAME);
            ois = new ObjectInputStream(fis);
            if (ois.readObject() == null) {
                Toast.makeText(this, "file is empty", Toast.LENGTH_SHORT).show();
            } else {
                colorsArray = (ArrayList<ColorButtonModel>) ois.readObject();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    public int getRandomColor(){
        int[] colors = {R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorRed};
        return colors[rand.nextInt(colors.length)];
    }

    public void initializeColorsArray(){
        for (int i = 0; i < columnsCount*columnsCount; i++){
            ColorButtonModel colorButtonModel = new ColorButtonModel();
            colorButtonModel.setColorResId(getRandomColor());
            colorButtonModel.isChecked = false;
            colorsArray.add(colorButtonModel);
        }
    }

    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonCount = 0;
            for (int i = 0; i < colorsArray.size(); i++) {
                ColorButtonModel colorButtonModel = colorsArray.get(i);
                colorButtonModel.isChecked = false;
                colorsArray.set(i, colorButtonModel);
            }
            getButtonsCount((Integer) v.getTag());
            Toast.makeText(MainActivity.this, "i: " + getI((Integer) v.getTag()) + " j: "
                    + getJ((Integer) v.getTag()) + " buttonCount: "
                    + buttonCount, Toast.LENGTH_LONG).show();
            if (buttonCount > 4) {
                for (int i = 0; i < colorsArray.size(); i++) {
                    ColorButtonModel model = colorsArray.get(i);
                    if (model.isChecked) {
                        model.setColorResId(R.color.colorBlack);
                    }
                }
                removeBlackButtons();
            }
        }
    };


    public int getI(int index) {
        return index/10;
    }

    public int getJ(int index) {
        return index%10;
    }



    int[] getButtonsCount(int index) {
        if (index == -1) {
            int[] indexesButton = {-1, -1, -1, -1};
            return indexesButton;
        }
        ColorButtonModel model = colorsArray.get(index);
        if (model.isChecked){
            int[] indexesButton = {-1, -1, -1, -1};
            return indexesButton;
        }
        int[] indexesButton = new int[4];
        buttonCount++;
        ColorButtonModel centerModel = colorsArray.get(index);

        if (index >= columnsCount) {
            ColorButtonModel topModel = colorsArray.get(index - columnsCount);
            if (centerModel.getColorResId() == topModel.getColorResId()) {
                indexesButton[0] = index - columnsCount;
            } else {
                indexesButton[0] = -1;
            }
        } else {
            indexesButton[0] = -1;
        }

        if (index % columnsCount < 9) {
            ColorButtonModel rightModel = colorsArray.get(index + 1);
            if (centerModel.getColorResId() == rightModel.getColorResId()) {
                indexesButton[1] = index + 1;
            } else {
                indexesButton[1] = -1;
            }
        } else {
            indexesButton[1] = -1;
        }

        if (index < columnsCount * (columnsCount - 1)) {
            ColorButtonModel bottomModel = colorsArray.get(index + columnsCount);
            if (centerModel.getColorResId() == bottomModel.getColorResId()) {
                indexesButton[2] = index + columnsCount;
            } else {
                indexesButton[2] = -1;
            }
        } else {
            indexesButton[2] = -1;
        }

        if (index % columnsCount > 0) {
            ColorButtonModel leftModel = colorsArray.get(index - 1);
            if (centerModel.getColorResId() == leftModel.getColorResId()) {
                indexesButton[3] = index - 1;
            } else {
                indexesButton[3] = -1;
            }
        } else {
            indexesButton[3] = -1;
        }
        centerModel.isChecked = true;
        getButtonsCount(indexesButton[0]);
        getButtonsCount(indexesButton[1]);
        getButtonsCount(indexesButton[2]);
        getButtonsCount(indexesButton[3]);
        return indexesButton;
    }

    void removeBlackButtons() {
        for (int j = 0; j < columnsCount; j++) {
            for (int i = columnsCount - 1; i >= 0; i--) {
                ColorButtonModel model = findColorButtonModel(i, j);
                if (model.getColorResId() == R.color.colorBlack) {
                    for (int k = i - 1; k >= 0; k--) {
                        ColorButtonModel bottomModel = findColorButtonModel(k + 1, j);
                        ColorButtonModel topModel = findColorButtonModel(k, j);
                        bottomModel.setColorResId(topModel.getColorResId());
                    }
                    findColorButtonModel(0, j).setColorResId(getRandomColor());
                    i++;
                }
            }
        }
        adapter.setColorsArray(colorsArray);
    }



    ColorButtonModel findColorButtonModel(int i, int j){
        ColorButtonModel model = colorsArray.get(i*columnsCount + j);
        return model;
    }


    static class ColorButtonModel {
        boolean isChecked;
        private int mColorResId;

        public void setColorResId(int colorResId) {
            if(colorResId < 0){
                Log.d("log","colorResId < 0");
            }
            this.mColorResId = colorResId;
        }

        public int getColorResId() {
            return mColorResId;
        }
    }

    class Coordinate {
        int row;
        int column;
        
    }
}
