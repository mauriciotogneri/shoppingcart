package com.mauriciotogneri.shoppinglist.views.productslist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mauriciotogneri.shoppinglist.R;
import com.mauriciotogneri.shoppinglist.adapters.ListProductAdapter;
import com.mauriciotogneri.shoppinglist.adapters.MenuItemAdapter;
import com.mauriciotogneri.shoppinglist.adapters.MenuItemAdapter.Option;
import com.mauriciotogneri.shoppinglist.model.Product;
import com.mauriciotogneri.shoppinglist.views.BaseView;
import com.mauriciotogneri.shoppinglist.widgets.CustomDialog;
import com.mauriciotogneri.shoppinglist.widgets.CustomImageView;

import java.util.ArrayList;
import java.util.List;

public class ProductsListView extends BaseView implements ProductsListViewInterface
{
    private ListProductAdapter listProductAdapter;

    @Override
    public void initialize(final Context context, final ProductsListViewObserver observer)
    {
        this.listProductAdapter = new ListProductAdapter(context);

        ListView listView = getListView(R.id.product_list);
        listView.setAdapter(this.listProductAdapter);

        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Product product = (Product) parent.getItemAtPosition(position);
                selectProduct(context, product, observer);
            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Product product = (Product) parent.getItemAtPosition(position);
                displayProductOptions(context, product, observer);

                return true;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void selectProduct(Context context, final Product product, final ProductsListViewObserver observer)
    {
        CustomDialog dialog = new CustomDialog(context, product.getName());
        dialog.setLayout(R.layout.dialog_cart_item);

        CustomImageView productImage = dialog.getCustomImageView(R.id.thumbnail);
        productImage.setImage(product.getImage());

        final NumberPicker quantity = dialog.getNumberPicker(R.id.quantity);
        quantity.setMinValue(1);
        quantity.setMaxValue(100);
        quantity.setValue(1);

        dialog.setPositiveButton(R.string.button_accept, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                observer.onAddProduct(product, quantity.getValue());
            }
        });

        dialog.setNegativeButton(R.string.button_cancel, null);

        dialog.display();
    }

    @SuppressLint("InflateParams")
    private void displayProductOptions(final Context context, final Product product, final ProductsListViewObserver observer)
    {
        final int EDIT_PRODUCT = 0;
        final int REMOVE_PRODUCT = 1;

        List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option(context.getString(R.string.icon_edit), context.getString(R.string.button_edit)));
        optionsList.add(new Option(context.getString(R.string.icon_remove), context.getString(R.string.button_remove)));

        CustomDialog dialog = new CustomDialog(context, product.getName());

        ListAdapter menuItemAdapter = new MenuItemAdapter(context, optionsList);
        dialog.setAdapter(menuItemAdapter, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int index)
            {
                switch (index)
                {
                    case EDIT_PRODUCT:
                        observer.onEditProduct(product);
                        break;

                    case REMOVE_PRODUCT:
                        removeProduct(context, product, observer);
                        break;
                }
            }
        });

        dialog.display();
    }

    @SuppressLint("InflateParams")
    private void removeProduct(Context context, final Product product, final ProductsListViewObserver observer)
    {
        CustomDialog dialog = new CustomDialog(context, product.getName());
        dialog.setLayout(R.layout.dialog_content_text);

        TextView text = dialog.getCustomTextView(R.id.text);
        text.setText(R.string.confirmation_remove_product);

        dialog.setPositiveButton(R.string.button_accept, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                observer.onRemoveProduct(product);
            }
        });

        dialog.setNegativeButton(R.string.button_cancel, null);

        dialog.display();
    }

    @Override
    public void showError(Context context)
    {
        showError(context, R.string.error_product_in_use);
    }

    @Override
    public void refreshList(List<Product> list)
    {
        listProductAdapter.refresh(list);

        ListView listView = getListView(R.id.product_list);
        TextView emptyLabel = getCustomTextView(R.id.empty_label);

        if (listProductAdapter.getCount() > 0)
        {
            listView.setVisibility(View.VISIBLE);
            listView.setSelection(0);
            emptyLabel.setVisibility(View.GONE);
        }
        else
        {
            listView.setVisibility(View.GONE);
            emptyLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getViewId()
    {
        return R.layout.view_products_list;
    }
}