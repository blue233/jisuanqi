package com.example.admin.jisuanqi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * 已计算的表达式
     */
    private String mPreStr = "";

    /**
     * 待计算的表达式
     */
    private String mLastStr = "";

    /**
     * 计算结果
     */
    private Object mResult = null;

    /**
     * 判断刚刚是否成功执行完一个表达式
     * 因为,刚加一个表达式时,需要在原来的表达式后面加上换行标签
     */
    private boolean mIsExecuteNow = false;

    /**
     * 换行符
     */
    private final String newLine = "<br>";

    /**
     * 操作按钮
     */
    private GridView mGridView = null;

    /**
     * 输入框
     */
    private EditText mEditInput = null;

    /**
     * 适配器
     */
    private ArrayAdapter mAdapter = null;

    /**
     * 操作按钮上的字符集合
     */
    private Button button;
    private final String[] mTextBtns = new String[]{
            "C","(",")","%",
            "7","8","9","÷",
            "4","5","6","×",
            "1","2","3","-",
            ".","0","=","+",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mEditInput = (EditText) findViewById(R.id.edit_input);
        mEditInput.setKeyListener(null);
        mGridView = (GridView) findViewById(R.id.grid_buttons);
        // 创建适配器
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTextBtns);
        // 设置适配器
        mGridView.setAdapter(mAdapter);
      /*  mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);

            }
        });*/
        mGridView.setOnItemClickListener(new OnButtonItemClickListener());
        button=(Button)findViewById(R.id.bt_science);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
        button=(Button)findViewById(R.id.bt_science);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
        final Button bt_menu=(Button)findViewById(R.id.bt_menu);
        bt_menu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showPopupMenu(bt_menu);
            }
        });
        Button btc=(Button)findViewById(R.id.bt_c);
        btc.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                /**
                 * 如果按下退格键,表示删除一个字符
                 * 如果待计算表达式长度为0,则需要把已计算表达式的最后
                 * 部分赋值给待计算表达式
                 *
                 * 例如：
                 *
                 * 已计算表达式：1+1=2
                 * 待计算表达式：空
                 *
                 * 按下Back键后：
                 *
                 * 已计算表达式：空
                 * 待计算表达式：1+1=2
                 *
                 */
                if (mLastStr.length() == 0) {
                    /**
                     * 如果已计算表达式的长度不是0,那么此时
                     * 已计算表达式必然以换行符结尾
                     */
                    if (mPreStr.length() != 0) {
                        // 清除已计算表达式末尾的换行符
                        mPreStr = mPreStr.substring(0, mPreStr.length() - newLine.length());
                        // 找到前一个换行符的位置
                        int index = mPreStr.lastIndexOf(newLine);
                        if (index == -1) {
                            /**
                             * -1表示没有找到,即 已计算表达式只有一个
                             * 此时把 仅有的一个已计算表达式赋值给待计算表达式
                             *
                             * 例如：
                             *
                             * 已计算表达式：1+1=2
                             * 待计算表达式：空
                             *
                             * 按下Back键后：
                             *
                             * 已计算表达式：空
                             * 待计算表达式：1+1=2
                             */
                            mLastStr = mPreStr;
                            // 已计算表达式赋值为空
                            mPreStr = "";
                        } else {
                            /**
                             * 如果找到前一个换行符,即 已计算表达式数量 > 1
                             * 此时把已计算表达式的最后一个表达式赋值给 待计算表达式
                             *
                             * 例如：
                             *
                             * 已计算表达式：1+1=2
                             *              2+2=4
                             * 待计算表达式：空
                             *
                             * 按下Back键后：
                             *
                             * 已计算表达式：1+1=2
                             * 待计算表达式：2+2=4
                             */
                            mLastStr = mPreStr.substring(index + newLine.length(), mPreStr.length());
                            mPreStr = mPreStr.substring(0, index + newLine.length());
                        }
                        mIsExecuteNow = true;
                    }
                } else {
                    // 如果待计算表达式长度不是0,则直接减掉一个字符
                    mLastStr = mLastStr.substring(0, mLastStr.length() - 1);
                }
                // 更新视图
                setText();
            }

        });
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_content, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("面积")){
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MainActivity.this,AreaActivity.class);
                    startActivity(intent);
                }
                if(item.getTitle().equals("长度")){
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MainActivity.this,LengthActivity.class);
                    startActivity(intent);
                }
                if(item.getTitle().equals("温度")){
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MainActivity.this,TemperatureActivity.class);
                    startActivity(intent);
                }
                if(item.getTitle().equals("体积")){
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MainActivity.this,VolumeActivity.class);
                    startActivity(intent);
                }
                if(item.getTitle().equals("质量")){
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MainActivity.this,QualityActivity.class);
                    startActivity(intent);
                }
                if(item.getTitle().equals("数据")){
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MainActivity.this,DataActivity.class);
                    startActivity(intent);
                }
                if(item.getTitle().equals("进制转换")){
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(MainActivity.this,JinzhiActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }
    /**
     * 这个函数用于设置EditText的显示内容,主要是为了加上html标签.
     * 所有的显示EditText内容都需要调用此函数
     */
    private void setText() {
        final String[] tags = new String[]{
                "<font color='#858585'>",
                "<font color='#CD2626'>",
                "</font> "
        };
        StringBuilder builder = new StringBuilder();
        // 添加颜色标签
        builder.append(tags[0]).append(mPreStr).append(tags[2]);
        builder.append(tags[1]).append(mLastStr).append(tags[2]);
        // 显示内容
        mEditInput.setText(Html.fromHtml(builder.toString()));
        // 设置光标在EditText的最后位置
        //  mEditInput.setSelection(mEditInput.getText().length());
    }
    /**
     * 执行 待计算表达式,当用户按下 = 号时,调用这个方法
     */
    private void executeExpression() {
        try {
            // 调用calc来执行运算
             mResult =calc.process(mLastStr);
        } catch (Exception e) {
            /**
             * 如果捕获到异常,表示表达式执行失败,
             * 这里设置为false是因为并没有执行成功,还不能新的表达式求值
                    */
            mIsExecuteNow = false;
            return;
        }
        /**
         * 若计算结果成功,把 "=" 和 结果 加到待计算表达式,
         * 更新视图,
         * 设置标识为true
         */
        mLastStr += "="+ mResult;
        setText();
        mIsExecuteNow = true;
    }
    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String text = (String) parent.getAdapter().getItem(position);
            if (text.equals("=")) {
                // 执行运算
                Toast.makeText(getApplicationContext(),mGridView.getItemAtPosition(position).toString(),
                        Toast.LENGTH_LONG).show();
                executeExpression();

            } else if (text.equals("C")) {
                /**
                 * 如果按下 清除键,就把
                 * 待计算表达式 和 已计算表达式 全部清空,
                 * 显示内容全部清空,
                 * 并设置标识符为false
                 *
                 * 这里需要注意下，
                 * 待计算表达式 和 已计算表达式 全部清空,
                 * 并不代表显示内容就清空了。同样，
                 * 显示内容清空，也不代表 待计算表达式 和 已计算表达式 全部清空。
                 */
                mPreStr = "";
                mLastStr = "";
                mIsExecuteNow = false;
                mEditInput.setText("");
            } else {
                // 按下其他键的情况
                if (mIsExecuteNow) {
                    /**
                     * 如果刚刚成功执行了一个表达式,
                     * 那么需要把  待计算表达式  加到  已计算表达式 后面并添加换行符
                     *
                     * 例如：
                     * 已计算表达式：空
                     * 待计算表达式：1+1=2
                     *
                     * 按下 1 键后：
                     *
                     * 已计算表达式：1+1=2
                     * 待计算表达式：1
                     *
                     */
                    mPreStr += mLastStr + newLine;
                    // 重置标识为false
                    mIsExecuteNow = false;
                    // 设置待计算表达式的第一个字符为当前按钮按下的内容
                    mLastStr = text;
                } else {
                    // 否则直接在待计算表达式后面添加内容就好了
                    mLastStr += text;
                }
                // 更新视图
                setText();
            }
        }
    }
}
