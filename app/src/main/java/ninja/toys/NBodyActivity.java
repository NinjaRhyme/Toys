package ninja.toys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import games.nbody.NBody;


//----------------------------------------------------------------------------------------------------
public class NBodyActivity extends AppCompatActivity {
    NBody m_nBody;

    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nbody);

        m_nBody = (NBody)findViewById(R.id.nBody);

        Button modeButton = (Button)findViewById(R.id.button_mode);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_nBody != null) {
                    m_nBody.setMode(m_nBody.getMode() == NBody.NBodyMode.NBODY_MODE_VIEW ? NBody.NBodyMode.NBODY_MODE_ACTION : NBody.NBodyMode.NBODY_MODE_VIEW);
                }
            }
        });
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nbody, menu);

        return true;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
