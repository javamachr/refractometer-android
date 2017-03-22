package cz.javamachr.refractometer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private DecimalFormat df = new DecimalFormat("#.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText corr = (EditText) findViewById(R.id.correctionFactor);
        corr.setText("1.04");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void calculateGravity(View view) {
        EditText corr = (EditText) findViewById(R.id.correctionFactor);
        Double corFact = Double.valueOf(corr.getText().toString());
        EditText text = (EditText)findViewById(R.id.startingGravity);
        String startingGravity = text.getText().toString();
        if (startingGravity != null && !"".equals(startingGravity)) {
            EditText textF = (EditText)findViewById(R.id.finalGravity);
            String finalGravity = textF.getText().toString();
            if (finalGravity != null && !"".equals(finalGravity)) {
                double starting = Double.valueOf(startingGravity) * corFact;
                double measured = Double.valueOf(finalGravity) * corFact;
                double val = calculateFinalGravity(starting, measured);
                TextView calcFG = (TextView)findViewById(R.id.calcFG);
                calcFG.setText(df.format(val)+" SG / "+df.format(sgToPlato(val))+" P");
                TextView calcAbv = (TextView)findViewById(R.id.calcAbv);
                calcAbv.setText(df.format(calculateAbv(calculateAbw(starting, measured), val))+" %");
            }
        }
        Snackbar.make(view, "Spocitano!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private double sgToPlato(double gravity) {
        return -460.234 + 662.649 * gravity - 202.41 * gravity * gravity;
    }

    private double platoToSG(double plato) {
        return  1 + (plato / (258.6 - ((plato/258.2) *227.1)));
    }

    private double calculateFinalGravity(double starting, double measured) {
        return -0.002349 * starting + 0.006276 * measured + 1;
    }

    private double calculateAbw(double starting, double measured) {
        return 0.67062 * starting - 0.66091 * measured;
    }

    private double calculateAbv(double abw, double measured) {
        return measured*abw / 0.791;
    }
}
