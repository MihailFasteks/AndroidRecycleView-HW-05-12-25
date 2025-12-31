package com.example.androidrecycleview;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import android.widget.*;

import com.bumptech.glide.Glide;

class Item {
    String URL;
    String name;
    String price;
    boolean checked;

    Item(String imageRes, String name, String price) {
        this.URL = imageRes;
        this.name = name;
        this.price = price;
    }
}
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final Item[] mDataset;
    private OnItemClickListener mItemClickListener;
    void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    // вьюхолдер буде зберігати посилання сформовані макети пунктів списку
    static class ViewHolder extends RecyclerView.ViewHolder {
        // пункт складається лише з одного TextView
// якщо будуть інші елементи, їх потрібно оформити як поля цього класу

        ImageView img;
        TextView name;
        TextView price;
        CheckBox check;
        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.photo);
            name = v.findViewById(R.id.name);
            price = v.findViewById(R.id.price);
            check = v.findViewById(R.id.check);
        }
    }
    RecyclerAdapter(Item[] dataset) {
        mDataset = dataset;
    }
    // створення нових вью (викликається леяут-менеджером)
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item, parent, false);
        return new ViewHolder(v);
    }
    // заміна контенту окремої вью (викликається леяут-менеджером)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mDataset[position];

        Glide.with(holder.img.getContext())
                .load(item.URL)
                .into(holder.img);

        holder.name.setText(item.name);
        holder.price.setText(item.price);

        holder.check.setOnCheckedChangeListener(null);
        holder.check.setChecked(item.checked);

        holder.check.setOnCheckedChangeListener((buttonView, isChecked) ->
                item.checked = isChecked
        );

        holder.itemView.setOnClickListener(v -> {
            boolean newState =!item.checked;
            item.checked=newState;
            holder.check.setChecked(newState);
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(position);
        });
    }
    // визначення кількості пунктів списку
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        var myDataset = getDataSet();
        RecyclerView rv = findViewById(R.id.my_recycler_view);
// якщо є впевненість, що зміни у контенті не змінять розмір
// леяута, то можна виставити true - це збільшує продуктивність
        rv.setHasFixedSize(true);
// призначення леяут-менеджера
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        lm = new GridLayoutManager(this, 2); // 2 стовпчика
        rv.setLayoutManager(lm);
// призначення адаптера
        var a = new RecyclerAdapter(myDataset);
// встановлення прослуховувача події натискання
        a.setOnItemClickListener(position -> {
            var selectedText = myDataset[position].name;
            Toast.makeText(MainActivity.this, "Обрано: " + selectedText, Toast.LENGTH_SHORT).show();
        });
        rv.setAdapter(a);
    }
    private Item[] getDataSet() {
        return new Item[] {
                new Item(
                        "https://ist.say7.info/img0009/93/993_013292e_5426_1024.jpg",
                        "Хлеб",
                        "25 грн"
                ),
                new Item(
                        "https://calorizator.ru/sites/default/files/imagecache/product_512/product/milk-33.jpg",
                        "Молоко",
                        "38 грн"
                ),
                new Item(
                        "https://syromaniya.ru/upload/iblock/688/7bxrxftyni4v6tj92xero2oa32yli8zi/Без%20имени-1.jpg",
                        "Сыр",
                        "120 грн"
                ),
                new Item(
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQGjITCu03AX_wnhok0l6u8Y6jimI-GJT7sFg&s",
                        "Яблоки",
                        "30 грн / кг"
                )
        };
    }
}