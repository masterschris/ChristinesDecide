package com.example.decisionmaker.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.decisionmaker.data.Choice;
import com.example.decisionmaker.data.ChoiceDataSource;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.widget.EditText;
import android.content.DialogInterface;


public class DecisionActivity extends ListActivity {

    private ChoiceDataSource dataSource;
    private List<Choice> choices;

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        dataSource = new ChoiceDataSource(this);

        registerForContextMenu(getListView());

        displayAllChoices();

        bindNewChoiceButton();

        bindDecideButton();

        bindExitButton();
    }

    private void displayAllChoices() {
        choices = dataSource.findAll();
        ArrayAdapter<Choice> adapter =
                new ArrayAdapter<Choice>(this, android.R.layout.simple_list_item_1, choices);
        setListAdapter(adapter);
    }

    private int currentChoiceId;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentChoiceId = (int) info.id;
        menu.add(0, 9999, 0, "Delete Choice");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 9999) {
            Choice choice = choices.get(currentChoiceId);
            dataSource.remove(choice);
            displayAllChoices();
        }

        return super.onContextItemSelected(item);

    }

    private void bindNewChoiceButton() {
        Button newChoiceButton = (Button) findViewById(R.id.new_choice);
        newChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChoice();
            }
        });
    }

    private void createChoice() {
        Choice choice = new Choice();
        updateChoiceFromInput(choice);
    }

    private void updateChoiceFromInput(final Choice choice) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setText(choice.getName());
        alert.setView(input);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String updatedName = input.getText().toString();
                choice.setName(updatedName);
                dataSource.save(choice);
                displayAllChoices();
            }
        });
        alert.show();
    }

    @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {
            Choice choice = choices.get(position);
            updateChoiceFromInput(choice);
    }

    private void decideOnChoices() {
        int randPosition = new Random().nextInt(choices.size());
        Choice selectedChoice = choices.get(randPosition);

        Toast.makeText(getApplicationContext(),
                "Decision has been made!\nGo with " + selectedChoice.getName(),
                Toast.LENGTH_SHORT).show();
    }

    private void bindDecideButton() {
        Button decideButton = (Button) findViewById(R.id.decide);
        decideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decideOnChoices();
            }
        });
    }

    private void bindExitButton() {
        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}