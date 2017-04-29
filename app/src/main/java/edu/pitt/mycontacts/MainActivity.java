package edu.pitt.mycontacts;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT = 0, DELETE = 1;

    EditText nameTxt, phoneTxt, emailTxt, AddressTxt;
    Button addbtn;

    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    DatabaseHandler dbHandler;
    int longClickedItemIndex;
    ArrayAdapter<Contact> contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        AddressTxt = (EditText) findViewById(R.id.txtAddress);
        contactListView = (ListView) findViewById(R.id.listview);
        dbHandler = new DatabaseHandler(getApplicationContext());

        registerForContextMenu(contactListView);

        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });



        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

    addbtn = (Button) findViewById(R.id.btnAdd);
    addbtn.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(AddressTxt.getText()));

                                      if (!contactExists(contact)) {
                                              dbHandler.createContact(contact);
                                              Contacts.add(contact);
                                              contactAdapter.notifyDataSetChanged();
                                              Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + "has been added!", Toast.LENGTH_SHORT).show();
                                              return;
                                          }
                                          Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + "exists", Toast.LENGTH_SHORT).show();
                                      }
                                  }
                                  );


        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // addbtn.setEnabled(String.valueOf(nameTxt.getText()).trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(dbHandler.getContactsCount() != 0)
            Contacts.addAll(dbHandler.getAllContacts());

        populateList();

    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle("Contact Options");
        menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Contact" );
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Contact" );
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case EDIT:

                break;
            case DELETE:
                 dbHandler.deleteContact(Contacts.get(longClickedItemIndex));
                Contacts.remove(longClickedItemIndex);
                contactAdapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private boolean contactExists(Contact contact){
        String name= contact.getName();
        int contactCount = Contacts.size();

        for (int i =0; i<contactCount ;i++){
            if (name.compareToIgnoreCase(Contacts.get(i).getName())== 0)
                return true;
        }
        return false;
    }

    private void populateList() {
        contactAdapter = new ContactListAdapter();
        contactListView.setAdapter(contactAdapter);
    }



private class ContactListAdapter extends ArrayAdapter<Contact> {
    public ContactListAdapter(){
super(MainActivity.this, R.layout.listview_item, Contacts);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        if(view == null)
            view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

        Contact currentContact = Contacts.get(position);

        TextView name = (TextView) view.findViewById(R.id.contactName);
        name.setText(currentContact.getName());
        TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
        phone.setText(currentContact.getPhone());
        TextView email = (TextView) view.findViewById(R.id.emailAddress);
        email.setText(currentContact.getEmail());
        TextView address = (TextView) view.findViewById(R.id.cAddress);
        address.setText(currentContact.getAddress());

        return view;
    }
}



}
