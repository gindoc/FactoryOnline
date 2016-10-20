package com.online.factory.factoryonline.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.online.factory.factoryonline.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Administrator
 * 
 *         <style name="dialog" parent="@android:style/Theme.Dialog"> <item name
 *         ="android:windowFrame">@null</item>
 *         <item name="android:windowIsFloating">true</item>
 *         <item name="android:windowIsTranslucent">true</item>
 *         <item name="android:windowNoTitle">true</item>
 *         <item name="android:windowBackground">@android:color/transparent
 *         </item>
 *         <item name="android:backgroundDimEnabled">true</item> </style>
 * 
 */
@SuppressWarnings("unused")
public class CustomDialog extends Dialog {

	public final static int TYPE_MESSAGE = 0;
	public final static int TYPE_EDIT = 1;
	public final static int TYPE_PROGRESS = 2;
	public final static int TYPE_RADIO = 3;
	public final static int TYPE_LOADING = 4;
	public final static int TYPE_ALERT = 5;
	public final static int TYPE_CHOOSE = 6;
	public final static int TYPE_CANCEL = 7;
	public final static int TYPE_POPUP_MESSAGE = 8;

	private int mTypeFlag = TYPE_MESSAGE;
	private View mRoot;
	private ReturnResults mReturnResult; // 返回结果
	int mWidth, mHeitht;

	private String mNotitleMessage; // 无标题对话框 信息

	private static Typeface sTypeface;

	public static void initTypeface(Typeface tf) {
		sTypeface = tf;
	}

	// 无标题对话框 构造方法

	// 有标题框（有返回）
	public static CustomDialog createMessageDialog(Context context, int message, ReturnResults result) {
		mCustomDialog = new CustomDialog(context, TYPE_CHOOSE, context.getResources().getString(message), result);
		return mCustomDialog;
	}

	public static CustomDialog createAlertDialog(Context context, int message, ReturnResults result) {
		mCustomDialog = new CustomDialog(context, TYPE_MESSAGE, context.getResources().getString(message), result);
		return mCustomDialog;
	}

	public static CustomDialog createMessageDialog(Context context, String message, ReturnResults result) {
		mCustomDialog = new CustomDialog(context, TYPE_MESSAGE, message, result);
		return mCustomDialog;
	}

	public static CustomDialog createLoadingDialog(Context context) {
		mCustomDialog = new CustomDialog(context, TYPE_LOADING);
		return mCustomDialog;
	}

	// 编辑框
	private CustomDialog(Context context, int type, String title, ReturnResults returnResult) {
		super(context, R.style.dialog);
		mTitle = title;
		mTypeFlag = type;
		mReturnResult = returnResult;
		init();
	}

	// 无标题框
	private CustomDialog(Context context, int type, String message) {
		super(context, R.style.dialog);
		mNotitleMessage = message;
		mTypeFlag = type;
		init();
	}

	// /////
	public CustomDialog(Context context, int type, String title, String message, String[] string,
			ReturnResults returnResult) {
		super(context, R.style.dialog);
		mTitle = message;
		mTypeFlag = type;
		mReturnResult = returnResult;
		init();
	}

	private String mText;
	static CustomDialog mCustomDialog;

	public static void dismissDialog() {
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
	}

	// 编辑对话框 构造方法
	public static CustomDialog createEditDialog(Context context, String title, String edit, ReturnResults result) {
		mCustomDialog = new CustomDialog(context, TYPE_EDIT, title, edit, result);
		return mCustomDialog;
	}

	public CustomDialog(Context context, int type, String title, String text, ReturnResults returnResult) {
		super(context, R.style.dialog);
		mTitle = title.toString();
		mTypeFlag = type;
		mReturnResult = returnResult;
		mText = text;
		init();
	}

	public static CustomDialog createChooseDialog(Context context, String title, String text,
			ReturnResults returnResult) {
		mCustomDialog = new CustomDialog(context, TYPE_CHOOSE, title, text, returnResult);
		return mCustomDialog;

	}

	// 编辑对话框 构造方法
	public static CustomDialog createEditDialog(Context context, String title, ReturnResults result) {
		mCustomDialog = new CustomDialog(context, TYPE_EDIT, new StringBuffer(title), result);
		return mCustomDialog;
	}

	public CustomDialog(Context context, int type, StringBuffer title, ReturnResults returnResult) {
		super(context, R.style.dialog);
		mTitle = title.toString();
		mTypeFlag = type;
		mReturnResult = returnResult;
		init();
	}

	private int mPrpgressCounts;// 进度条对话框 时间

	// 进度条对话框 构造方法

	public CustomDialog(Context context, int type, int counts) {
		super(context, R.style.dialog);
		mPrpgressCounts = counts;
		mTypeFlag = type;
		init();
	}

	private String mRadioTitle; // 单选对话框 标题
	private List<String> mRadioItems; // 单选对话框 选项

	// 单选对话框 构造方法

	// public st

	public static CustomDialog createSelecterDialog(Context context, String title, List<String> radioItems,
			ReturnResults returnResult) {
		mCustomDialog = new CustomDialog(context, TYPE_RADIO, title, radioItems, returnResult);
		return mCustomDialog;
	}

	public static CustomDialog createSelecterDialog(Context context, String title, String[] radioItems,
			ReturnResults returnResult) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < radioItems.length; i++) {
			list.add(radioItems[i]);
		}
		return createSelecterDialog(context, title, list, returnResult);
	}

	//
	public CustomDialog(Context context, int type, String title, List<String> radioItems, ReturnResults returnResult) {
		super(context, R.style.dialog);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mTitle = title;
		mTypeFlag = type;
		mRadioItems = radioItems;
		mReturnResult = returnResult;

		init();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		mCustomDialog = null;
	}

	public CustomDialog(Context context, int type) {
		super(context, R.style.dialog);
		mTypeFlag = type;
		init();
	}

	@Override
	public void show() {
		if (mCustomDialog != null) {
			super.show();
		}
	}

	String mAlertTitle;
	String mAlertMessage;

	public static CustomDialog createAlertDialog(Context context, String title, String message) {
		mCustomDialog = new CustomDialog(context, TYPE_ALERT, title, message);
		return mCustomDialog;

	}

	public static CustomDialog createCancelDialog(Context context) {
		mCustomDialog = new CustomDialog(context, TYPE_ALERT);
		return mCustomDialog;

	}

	public CustomDialog(Context context, int type, String title, String message) {
		super(context, R.style.dialog);
		mTitle = title;
		mAlertMessage = message;
		mTypeFlag = type;

		init();
	}

	public static CustomDialog createPopupMessageDialog(Context context, String title, String message) {
		mCustomDialog = new CustomDialog(context, TYPE_POPUP_MESSAGE, title, message);
		return mCustomDialog;
	}

	String mTitle;

	public void init() {
		mWidth = Density.getInstence(getContext()).getScreenWidth();
		mHeitht = Density.getInstence(getContext()).getScreenHeight();
		switch (mTypeFlag) {
		case TYPE_EDIT:
			String[] s = { "NO", "YES" };
			mRoot = new StypeEdit(getContext(), mTitle, mText, s, mReturnResult);
			break;
		case TYPE_MESSAGE:
			mRoot = new DialogComfirm(getContext(), mTitle, mReturnResult);
			break;
		case TYPE_PROGRESS:
			String titleText = "下载信息";
			mRoot = new StypeProgress(getContext(), mPrpgressCounts, titleText);
			break;
		case TYPE_RADIO:
			List<String> listItems = new ArrayList<String>();
			for (int i = 0; i < 15; i++) {
				listItems.add("Item" + i);
			}
			String radiobutton = "确定";
			mRoot = new StypeRadio(getContext(), mTitle, mRadioItems, radiobutton, mReturnResult);
			break;
		case TYPE_LOADING:
			String text = "正在加载中...";
			mRoot = new StypeLoading(getContext(), text);
			break;
		case TYPE_CHOOSE:
			// String text = "正在加载中...";
			mRoot = new DialogChoose(getContext(), mTitle, mText, mReturnResult);
			break;
		case TYPE_ALERT:
			// String text = "正在加载中...";

			mRoot = new DialogAlert(getContext(), mTitle, mAlertMessage);
			break;
		case TYPE_CANCEL:
			// String text = "正在加载中...";

			mRoot = new StyCancel(getContext());
			break;
		case TYPE_POPUP_MESSAGE:
			// String text = "正在加载中...";

			mRoot = new DialogPopupMessage(getContext(), mTitle, mAlertMessage);
			break;

		default:
			break;
		}
		setContentView(mRoot);
	}

	class DialogPopupMessage extends FrameLayout {
		String title = "title";
		String message = "message";
		Context context;

		public DialogPopupMessage(Context context, String title, String message) {
			super(context);
			this.context = context;
			this.title = title;
			this.message = message;
			initView();
		}

		@SuppressWarnings("deprecation")
		public void initView() {
			LinearLayout.LayoutParams contextParams = new LinearLayout.LayoutParams((int) (mWidth * 0.9),
					LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout linearlayout = new LinearLayout(context);
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			linearlayout.setGravity(Gravity.CENTER);
			linearlayout.setOrientation(LinearLayout.VERTICAL);

			LinearLayout.LayoutParams ButtonParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(context).dip2px(60));
			// ButtonParams.bottomMargin =
			// Density.getInstence(context).dip2px(10);
			// linearlayout.setBackgroundDrawable(new BackDrawable("linear"));

			TextView titleView = new TextView(context);
			titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F);
			CustomDrawables customs = new CustomDrawables(context, (int) (mWidth * 0.9),
					Density.getInstence(context).dip2px(80), 0);
			titleView.setBackgroundDrawable(customs.paintDrawable());

			titleView.setText(title);
			titleView.setTextColor(Color.BLACK);

			int pading = Density.getInstence(context).dip2px(10);
			titleView.setPadding(pading, pading, pading, pading);
			View view = new View(context);

			TextView messageView = new TextView(context);
			messageView.setText(message);
			messageView.setBackgroundColor(Color.WHITE);
			messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			messageView.setTextColor(Color.BLACK);
			messageView.setPadding(pading, pading, pading, pading);
			messageView.setMinHeight((int) (mWidth * 0.6));

			Button okButton = new Button(context);
			okButton.setGravity(Gravity.CENTER);
			okButton.setText("OK");
			okButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
			okButton.setTextColor(0xff241e5a);
			StateListDrawable state = new StateListDrawable();
			state.addState(new int[] { android.R.attr.state_pressed }, new BackDrawable("button_1"));
			state.addState(new int[] { -android.R.attr.state_pressed }, new BackDrawable("button_0"));

			okButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			linearlayout.addView(titleView, linearParams);
			view.setBackgroundColor(0xffcccccc);

			LinearLayout buttonlinear = new LinearLayout(context);
			buttonlinear.setGravity(Gravity.CENTER);
			CustomDrawables custom = new CustomDrawables(context, (int) (mWidth * 0.9),
					Density.getInstence(context).dip2px(60), CustomDrawables.TYPE_BUTTON);
			okButton.setBackgroundDrawable(custom.paintDrawable());
			buttonlinear.addView(okButton, ButtonParams);
			linearlayout.addView(messageView, linearParams);
			linearlayout.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(context).dip2px(1));
			linearlayout.addView(buttonlinear, linearParams);
			addView(linearlayout, contextParams);
			if (sTypeface != null) {
				titleView.setTypeface(sTypeface);
				messageView.setTypeface(sTypeface);
				okButton.setTypeface(sTypeface);
			}
		}

		class BackDrawable extends Drawable {
			int corners = Density.getInstence(context).dip2px(5);
			float[] cornersRect = { corners, corners, corners, corners, corners, corners, corners, corners };
			String s;

			public BackDrawable(String s) {
				this.s = s;
			}

			@Override
			public void setColorFilter(ColorFilter colorFilter) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setAlpha(int alpha) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getOpacity() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void draw(Canvas canvas) {
				ShapeDrawable myShapeDrawable = new ShapeDrawable(new RoundRectShape(cornersRect, null, null));
				myShapeDrawable.getPaint().setAntiAlias(true);
				if (s.equals("linear")) {

					myShapeDrawable.getPaint().setColor(Color.WHITE);
					myShapeDrawable.setBounds(0, 0, (int) (mWidth * 0.9), getHeight());
				} else if (s.endsWith("button_0")) {//

					// myShapeDrawable.getPaint().setColor(Color.WHITE);
					// myShapeDrawable.setBounds(0, 0, (int) (mWidth * 0.5),
					// Density.getInstence(context).dip2px(35));
					myShapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
					myShapeDrawable.getPaint().setStrokeWidth(Density.getInstence(context).dip2px(1));
					myShapeDrawable.getPaint().setColor(0xff241e5a);
					myShapeDrawable.setBounds(0, 0, (int) (mWidth * 0.7 / 2), Density.getInstence(context).dip2px(35));
				} else if (s.endsWith("button_1")) {

					// myShapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
					// myShapeDrawable.getPaint().setStrokeWidth(Density.getInstence(context).dip2px(2));
					// myShapeDrawable.getPaint().setColor(0xff241e5a);
					// myShapeDrawable.setBounds(0, 0, (int) (mWidth * 0.5),
					// Density.getInstence(context).dip2px(35));
					myShapeDrawable.getPaint().setStyle(Paint.Style.FILL);
					myShapeDrawable.getPaint().setColor(0xffcccccc);
					myShapeDrawable.setBounds(0, 0, (int) (mWidth * 0.7 / 2), Density.getInstence(context).dip2px(35));

				}

				myShapeDrawable.draw(canvas);

			}
		};
	}

	class StypeEdit extends FrameLayout {
		String[] mSures;
		ReturnResults mReturnResult;
		String mTitle;
		String mText;
		Context mContext;

		public StypeEdit(Context context, String title, String text, String[] sure, ReturnResults returnResult) {
			super(context);
			// setBackgroun
			mContext = context;
			mSures = sure;
			mTitle = title;
			mText = text;
			mReturnResult = returnResult;
			initView();
		}

		@SuppressWarnings("deprecation")
		public void initView() {
			CustomDrawables custonm = new CustomDrawables(mContext, (int) (mWidth * 0.7f),
					Density.getInstence(mContext).dip2px(60), 0);
			LinearLayout linear = new LinearLayout(mContext);
			linear.setOrientation(LinearLayout.VERTICAL);
			// linear.setBackgroundResource(R.drawable.linear_shape);
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7f),
					LinearLayout.LayoutParams.WRAP_CONTENT);
			TextView title = new TextView(mContext);
			// title.setBackgroundResource(R.drawable.title_shape);
			title.setBackgroundDrawable(custonm.paintDrawable());
			int padding = Density.getInstence(mContext).dip2px(10);
			title.setPadding(padding, padding, padding, padding);
			title.setText(mTitle);
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.BLACK);
			TextPaint tp = title.getPaint();
			tp.setFakeBoldText(true);
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			final EditText edit = new EditText(mContext);
			edit.setBackgroundResource(android.R.color.white);
			edit.setTextColor(Color.BLACK);
			if (!TextUtils.isEmpty(mText)) {
				edit.setText(mText);
				edit.setSelection(mText.length());
			}
			LinearLayout sureLinear = new LinearLayout(mContext);
			LinearLayout.LayoutParams sureLinearParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(mContext).dip2px(50));
			sureLinearParams.weight = 1;
			Button cancelButton = new Button(mContext);
			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
			custonm.setFlag(CustomDrawables.TYPE_LEFT_BUTTON);
			// cancelButton.setBackgroundResource(R.drawable.button_style_left);
			cancelButton.setBackgroundDrawable(custonm.paintDrawable());
			cancelButton.setTextColor(0xff5379b8);
			Button sureButton = new Button(mContext);
			// sureButton.setBackgroundResource(R.drawable.button_style_right);
			custonm.setFlag(CustomDrawables.TYPE_RIGHT_BUTTON);
			sureButton.setBackgroundDrawable(custonm.paintDrawable());
			sureButton.setTextColor(0xff5379b8);
			cancelButton.setText(mSures[0]);
			sureButton.setPadding(padding, padding, padding, padding);
			cancelButton.setPadding(padding, padding, padding, padding);
			cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			sureButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			sureButton.setText(mSures[1]);
			sureButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!TextUtils.isEmpty(edit.getText())) {
						String message = edit.getText().toString();
						mReturnResult.result(message);
						dismiss();
					}
				}
			});
			sureLinear.addView(cancelButton, sureLinearParams);
			sureLinear.addView(sureButton, sureLinearParams);
			linear.addView(title, sureLinearParams);
			linear.addView(edit, linearParams);
			linear.addView(sureLinear, linearParams);
			this.addView(linear);
			if (sTypeface != null) {
				title.setTypeface(sTypeface);
				edit.setTypeface(sTypeface);
				cancelButton.setTypeface(sTypeface);
				sureButton.setTypeface(sTypeface);
			}

		}

	}

	class DialogChoose extends FrameLayout {
		String mTitle = "title";
		String mMessage = "messgemessgemessgemessgemessgemessgemessgemessgemessgemessge";
		String[] buttonText = { "YES", "NO" };
		Context mContext;
		ReturnResults results;

		public DialogChoose(Context context, String title, String message, ReturnResults results) {
			super(context);
			mContext = context;
			mTitle = title;
			mMessage = message;
			this.results = results;
			initView();
		}

		@SuppressWarnings("deprecation")
		public void initView() {
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7),
					LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout linearLayout = new LinearLayout(mContext);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			TextView titleText = new TextView(mContext);
			titleText.setText(mTitle);
			int padding = Density.getInstence(mContext).dip2px(10);
			titleText.setPadding(padding, padding, padding, 0);
			titleText.setGravity(Gravity.CENTER);
			titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
			titleText.setTextColor(Color.BLACK);
			TextPaint tp = titleText.getPaint();
			tp.setFakeBoldText(true);
			titleText.setGravity(Gravity.CENTER);
			CustomDrawables customs = new CustomDrawables(getContext(), (int) (mWidth * 0.7),
					Density.getInstence(mContext).dip2px(60), 5);
			titleText.setBackgroundDrawable(customs.paintDrawable());
			TextView messageText = new TextView(mContext);
			messageText.setText(mMessage + "?");
			messageText.setTextColor(Color.BLACK);
			messageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			messageText.setGravity(Gravity.CENTER);
			messageText.setBackgroundColor(Color.WHITE);
			messageText.setPadding(padding, 0, padding, padding);
			LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7),
					Density.getInstence(mContext).dip2px(50));
			buttonParams.weight = 1;
			LinearLayout buttonLayout = new LinearLayout(mContext);
			buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
			Button noText = new Button(mContext);
			customs.setFlag(CustomDrawables.TYPE_LEFT_BUTTON);
			noText.setBackgroundDrawable(customs.paintDrawable());
			noText.setGravity(Gravity.CENTER);
			noText.setTextColor(0xff5379b8);
			noText.setText(buttonText[1]);
			noText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			noText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					results.result("no");
					dismiss();
				}
			});
			Button yesText = new Button(mContext);
			yesText.setTextColor(0xff5379b8);
			yesText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F);
			yesText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					results.result("yes");
					dismiss();
				}
			});

			customs.setFlag(CustomDrawables.TYPE_RIGHT_BUTTON);
			yesText.setText(buttonText[0]);
			yesText.setGravity(Gravity.CENTER);
			yesText.setBackgroundDrawable(customs.paintDrawable());
			buttonLayout.addView(noText, buttonParams);
			View btnView = new View(mContext);
			btnView.setBackgroundColor(0xffcccccc);
			buttonLayout.addView(btnView, Density.getInstence(mContext).dip2px((float) 0.5),
					LinearLayout.LayoutParams.MATCH_PARENT);
			buttonLayout.addView(yesText, buttonParams);
			linearLayout.addView(titleText, LinearLayout.LayoutParams.MATCH_PARENT,
					Density.getInstence(mContext).dip2px(50));
			View titleView = new View(mContext);
			titleView.setBackgroundColor(0xffcccccc);
			// linearLayout.addView(titleView,
			// LinearLayout.LayoutParams.MATCH_PARENT,
			// Density.getInstence(mContext).dip2px((float) 0.5));
			linearLayout.addView(messageText);
			View messageView = new View(mContext);
			messageView.setBackgroundColor(0xffcccccc);
			linearLayout.addView(messageView, LinearLayout.LayoutParams.MATCH_PARENT,
					Density.getInstence(mContext).dip2px((float) 0.5));
			linearLayout.addView(buttonLayout);
			addView(linearLayout, linearParams);
			if (sTypeface != null) {
				titleText.setTypeface(sTypeface);
				messageText.setTypeface(sTypeface);
				noText.setTypeface(sTypeface);
				yesText.setTypeface(sTypeface);
			}
		}

	}

	class DialogAlert extends FrameLayout {
		String mTitle = "title";
		String mMessage = "messge";
		String buttonText = "OK";
		Context mContext;

		public DialogAlert(Context context, String title, String message) {
			super(context);
			mContext = context;
			mTitle = title;
			mMessage = message;
			initView();
		}

		public void initView() {
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7),
					LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout linearLayout = new LinearLayout(mContext);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			TextView titleText = new TextView(mContext);
			titleText.setText(mTitle);
			TextPaint tp = titleText.getPaint();
			titleText.setTextColor(Color.BLACK);
			tp.setFakeBoldText(true);
			int padding = Density.getInstence(mContext).dip2px(10);
			titleText.setPadding(padding, padding, padding, 0);
			titleText.setGravity(Gravity.CENTER);
			titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
			titleText.setGravity(Gravity.CENTER);
			CustomDrawables customs = new CustomDrawables(getContext(), (int) (mWidth * 0.7),
					Density.getInstence(mContext).dip2px(60), 5);
			titleText.setBackgroundDrawable(customs.paintDrawable());
			TextView messageText = new TextView(mContext);
			messageText.setText(mMessage);
			messageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			messageText.setGravity(Gravity.CENTER);
			messageText.setBackgroundColor(Color.WHITE);
			LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7),
					Density.getInstence(mContext).dip2px(50));
			messageText.setTextColor(Color.BLACK);
			messageText.setPadding(padding, 0, padding, padding);
			Button Text = new Button(mContext);
			Text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
			customs.setFlag(CustomDrawables.TYPE_BUTTON);
			// CustomDrawables customs1 = new CustomDrawables(getContext(),
			// (int) (mWidth * 0.7),
			// Density.getInstence(mContext).dip2px(40),
			// CustomDrawables.TYPE_LEFT_BUTTON);
			Text.setBackgroundDrawable(customs.paintDrawable());
			Text.setGravity(Gravity.CENTER);
			Text.setText(buttonText);
			Text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F);
			Text.setTextColor(0xff5379b8);
			linearLayout.addView(titleText, LinearLayout.LayoutParams.MATCH_PARENT,
					Density.getInstence(mContext).dip2px(50));
			View titleView = new View(mContext);
			titleView.setBackgroundColor(0xffcccccc);
			// linearLayout.addView(titleView,
			// LinearLayout.LayoutParams.MATCH_PARENT,
			// Density.getInstence(mContext).dip2px((float) 0.5));
			linearLayout.addView(messageText);
			View messageView = new View(mContext);
			messageView.setBackgroundColor(0xffcccccc);
			linearLayout.addView(messageView, LinearLayout.LayoutParams.MATCH_PARENT,
					Density.getInstence(mContext).dip2px((float) 0.5));
			linearLayout.addView(Text, buttonParams);
			addView(linearLayout, linearParams);
			if (sTypeface != null) {
				titleText.setTypeface(sTypeface);
				messageText.setTypeface(sTypeface);
				Text.setTypeface(sTypeface);
			}
		}

	}

	class ButtonStateColor extends Drawable {
		int direction = 0;
		boolean ischeck = false;
		int mCorner = Density.getInstence(getContext()).dip2px(8);
		float[] jouterR;
		int w;
		int color = Color.WHITE;

		public ButtonStateColor(boolean ischeck, int direction) {
			this.direction = direction;
			this.ischeck = ischeck;
		}

		@Override
		public void draw(Canvas canvas) {
			if (ischeck) {
				switch (direction) {
				case 0:
					w = (int) (mWidth * 0.7);
					jouterR = new float[] { 0, 0, 0, 0, mCorner, mCorner, mCorner, mCorner };
					break;
				case 1:
					w = (int) (mWidth * 0.7) / 2;
					jouterR = new float[] { 0, 0, 0, 0, 0, 0, mCorner, mCorner };
					break;
				case 2:
					w = (int) (mWidth * 0.7) / 2;
					jouterR = new float[] { 0, 0, 0, 0, mCorner, mCorner, 0, 0 };
					break;
				case 3:
					w = (int) (mWidth * 0.7);
					jouterR = new float[] { mCorner, mCorner, mCorner, mCorner, 0, 0, 0, 0 };
					break;

				default:
					break;
				}

			} else {
				color = Color.TRANSPARENT;
				switch (direction) {
				case 0:
					w = (int) (mWidth * 0.7);
					jouterR = new float[] { 0, 0, 0, 0, mCorner, mCorner, mCorner, mCorner };
					break;
				case 1:
					w = (int) (mWidth * 0.7) / 2;
					jouterR = new float[] { 0, 0, 0, 0, 0, 0, mCorner, mCorner };
					break;
				case 2:
					w = (int) (mWidth * 0.7) / 2;
					jouterR = new float[] { 0, 0, 0, 0, mCorner, mCorner, 0, 0 };
					break;

				default:
					break;
				}
				ShapeDrawable myShapeDrawable = new ShapeDrawable(new RoundRectShape(jouterR, null, null));
				myShapeDrawable.getPaint().setColor(color);
				myShapeDrawable.setBounds(0, 0, w, Density.getInstence(getContext()).dip2px(50));
				myShapeDrawable.draw(canvas);
			}
		}

		@Override
		public void setAlpha(int alpha) {

		}

		@Override
		public void setColorFilter(ColorFilter colorFilter) {

		}

		@Override
		public int getOpacity() {
			return 0;
		}
	}

	class DialogComfirm extends FrameLayout {
		Context mContext;
		String mMessage;

		public DialogComfirm(Context context, String message, ReturnResults results) {
			super(context);
			mContext = context;
			mMessage = message;
			initView();
		}

		@SuppressWarnings("deprecation")
		public void initView() {
			CustomDrawables custom = new CustomDrawables(mContext, (int) (mWidth * 0.7),
					Density.getInstence(mContext).dip2px(110), CustomDrawables.TYPE_PROGRESS);
			LinearLayout linear = new LinearLayout(mContext);
			linear.setBackgroundDrawable(custom.paintDrawable());
			linear.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7f),
					LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams contextParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(mContext).dip2px(60));
			contextParams.topMargin = Density.getInstence(mContext).dip2px(10);
			contextParams.leftMargin = Density.getInstence(mContext).dip2px(20);
			contextParams.rightMargin = Density.getInstence(mContext).dip2px(20);
			contextParams.bottomMargin = Density.getInstence(mContext).dip2px(5);

			TextView messText = new TextView(mContext);
			messText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);
			messText.setText(mMessage);
			messText.setTextColor(Color.BLACK);
			messText.setGravity(Gravity.CENTER_VERTICAL);
			linear.addView(messText, contextParams);
			View messageView = new View(mContext);
			messageView.setBackgroundColor(0xffcccccc);
			LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					Density.getInstence(mContext).dip2px((float) 0.5));
			 linear.addView(messageView, viewParams);
			int padding = Density.getInstence(mContext).dip2px(10);
			LinearLayout sureLinear = new LinearLayout(mContext);
			LinearLayout.LayoutParams sureLinearParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(mContext).dip2px(50));
			sureLinearParams.weight = 1;
			Button cancelButton = new Button(mContext);
			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
			custom.setFlag(CustomDrawables.TYPE_LEFT_BUTTON);
			// cancelButton.setBackgroundResource(R.drawable.button_style_left);
			cancelButton.setBackgroundDrawable(custom.paintDrawable());
			cancelButton.setTextColor(0xff5379b8);
			Button sureButton = new Button(mContext);
			// sureButton.setBackgroundResource(R.drawable.button_style_right);
			custom.setFlag(CustomDrawables.TYPE_RIGHT_BUTTON);
			sureButton.setBackgroundDrawable(custom.paintDrawable());
			sureButton.setTextColor(0xff5379b8);
			cancelButton.setText("NO");
			sureButton.setPadding(padding, padding, padding, padding);
			cancelButton.setPadding(padding, padding, padding, padding);
			cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			sureButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			sureButton.setText("YES");
			sureButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mReturnResult.result("YES");
					dismiss();
				}
			});
			View btnView = new View(mContext);
			btnView.setBackgroundColor(0xffcccccc);
			sureLinear.addView(cancelButton, sureLinearParams);
			sureLinear.addView(btnView, Density.getInstence(mContext).dip2px((float) 0.5),
					Density.getInstence(mContext).dip2px(50));
			sureLinear.addView(sureButton, sureLinearParams);
		
			linear.addView(sureLinear, linearParams);

			this.addView(linear, linearParams);
			if (sTypeface != null) {
				messText.setTypeface(sTypeface);
				cancelButton.setTypeface(sTypeface);
				sureButton.setTypeface(sTypeface);
			}

		}

	}

	class StypeProgress extends FrameLayout {
		int mMaxlength;
		Context mContext;
		ProgressBar mProgress;
		int mProgressColor = 0xff69af2a;
		int mProgressBackColor = Color.BLACK;
		int time = 1;
		String titleText = "下载信息";

		public StypeProgress(Context context, int length, String titleText) {
			super(context);
			mMaxlength = 60;
			mContext = context;
			this.titleText = titleText;
			initView();
		}

		int mlength, progressWidth;

		@SuppressWarnings("deprecation")
		public void initView() {
			LinearLayout linear = new LinearLayout(mContext);
			linear.setOrientation(LinearLayout.VERTICAL);
			CustomDrawables custom = new CustomDrawables(mContext, (int) (mWidth * 0.7),
					Density.getInstence(mContext).dip2px(100), CustomDrawables.TYPE_PROGRESS);
			// linear.setBackgroundResource(R.drawable.linear_shape);
			linear.setBackgroundDrawable(custom.paintDrawable());
			linear.setOrientation(LinearLayout.VERTICAL);
			// linear.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7f),
					Density.getInstence(mContext).dip2px(100));
			LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7f),
					LinearLayout.LayoutParams.WRAP_CONTENT);
			titleParams.bottomMargin = Density.getInstence(mContext).dip2px(10);
			titleParams.leftMargin = Density.getInstence(mContext).dip2px(15);
			titleParams.topMargin = Density.getInstence(mContext).dip2px(20);
			titleParams.rightMargin = Density.getInstence(mContext).dip2px(5);
			mlength = (int) (mWidth * 0.77) / mMaxlength;
			progressWidth = mlength;
			TextView title = new TextView(mContext);
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			if (sTypeface != null) {
				title.setTypeface(sTypeface);

			}
			title.setText("下载信息");
			linear.addView(title, titleParams);
			mProgress = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
			mProgress.setProgressDrawable(new Drawable() {

				@Override
				public void setColorFilter(ColorFilter colorFilter) {

				}

				@Override
				public void setAlpha(int alpha) {

				}

				@Override
				public int getOpacity() {
					return 0;
				}

				@Override
				public void draw(Canvas canvas) {
					Paint p = new Paint();
					p.setAntiAlias(true);
					p.setColor(0xff3a3a3a);
					int r = Density.getInstence(mContext).dip2px(3);
					int rp = Density.getInstence(mContext).dip2px(1);
					RectF backrect = new RectF(0, 0, (float) (mWidth * 0.77), Density.getInstence(mContext).dip2px(16));

					canvas.drawRoundRect(backrect, r, r, p);
					p.setColor(mProgressColor);

					RectF rect = new RectF(1, Density.getInstence(mContext).dip2px(1), progressWidth,
							Density.getInstence(mContext).dip2px(15));
					canvas.drawRoundRect(rect, rp, rp, p);

					p.setColor(Color.BLACK);
					p.setTextSize(Density.getInstence(mContext).sp2px(8));
					if (time < mMaxlength) {
						canvas.drawText(time + "%", progressWidth - Density.getInstence(mContext).dip2px(6),
								Density.getInstence(mContext).dip2px(11), p);
					}

					progressWidth = progressWidth + mlength;

					if (progressWidth < mWidth * 0.77) {

						++time;
						if (time > mMaxlength) {
							time = mMaxlength;
						}
						invalidateSelf();
						try {
							if (time > 3)
								Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			});

			mProgress.setMax(mMaxlength);

			LinearLayout.LayoutParams contextParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			contextParams.leftMargin = Density.getInstence(mContext).dip2px(5);
			contextParams.rightMargin = Density.getInstence(mContext).dip2px(5);
			linear.addView(mProgress, contextParams);

			addView(linear, linearParams);

		}

	}

	class StypeRadio extends FrameLayout {
		private Context mContext;
		ReturnResults mReturnResult;
		List<String> mRadioItems;
		String mRadioTitle, mRadioButton;
		int position = 0;

		public StypeRadio(Context context, String title, List<String> string, String button,
				ReturnResults returnResult) {
			super(context);
			mContext = context;
			mRadioItems = string;
			mRadioTitle = title;
			mReturnResult = returnResult;
			mRadioButton = button;
			initView();
		}

		@SuppressWarnings("deprecation")
		public void initView() {
			LinearLayout linear = new LinearLayout(mContext);
			CustomDrawables custom = new CustomDrawables(mContext, (int) (mWidth * 0.85),
					Density.getInstence(mContext).dip2px(50), CustomDrawables.TYPE_TITLE);
			linear.setOrientation(LinearLayout.VERTICAL);
			linear.setGravity(Gravity.CENTER);
			// linear.setBackgroundResource(R.drawable.linear_shape);
			// linear.setBackground(custom.paintDrawable());
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.85f),
					(int) (mHeitht * 0.7f));
			TextView textView = new TextView(mContext);
			ListView listView = new ListView(mContext);

			listView.setBackgroundColor(Color.WHITE);

			int padding = Density.getInstence(mContext).dip2px(10);
			textView.setPadding(padding, padding, padding, padding);
			textView.setText(mRadioTitle);
			TextPaint tp = textView.getPaint();
			tp.setFakeBoldText(true);
			textView.setTextColor(Color.BLACK);
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundDrawable(custom.paintDrawable());
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			LinearLayout.LayoutParams contextParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(mContext).dip2px(50));
			linear.addView(textView, contextParams);
			linear.addView(listView, LinearLayout.LayoutParams.MATCH_PARENT, (int) (mHeitht * 0.3f));
			Button sureButton = new Button(mContext);
			sureButton.setText(mRadioButton);
			sureButton.setClickable(false);
			sureButton.setTextColor(0xff5379b8);
			sureButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			listView.setVerticalScrollBarEnabled(false);
			custom.setFlag(CustomDrawables.TYPE_BUTTON);
			sureButton.setBackgroundDrawable(custom.paintDrawable());
			sureButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
					mReturnResult.result(mRadioItems.get(position));
				}
			});
			// sureButton.setBackgroundResource(R.drawable.button_style_left);
			linear.addView(sureButton, LinearLayout.LayoutParams.MATCH_PARENT,
					Density.getInstence(mContext).dip2px(50));

			final ListViewAdapter mListViewAdapter = new ListViewAdapter(mContext, mRadioItems);
			listView.setAdapter(mListViewAdapter);
			StateListDrawable state = new StateListDrawable();

			state.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(0xffcccccc));

			listView.setSelector(state);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int positio, long id) {
					mListViewAdapter.setSelectID(positio); // 选中位置
					mListViewAdapter.notifyDataSetChanged(); // 刷新适配器
					position = positio;
					// RadioButton rb = (RadioButton)
					// view.findViewById(0x10002);
					// rb.setChecked(true);
					// mReturnResult.result(mRadioItems.get(position));

				}
			});
			if (sTypeface != null) {
				textView.setTypeface(sTypeface);
				sureButton.setTypeface(sTypeface);
			}

			// 自定义回调函数
			mListViewAdapter.setOncheckChanged(new OnMyCheckChangedListener() {

				@Override
				public void setSelectID(int selectID) {
					mListViewAdapter.setSelectID(selectID); // 选中位置
					mListViewAdapter.notifyDataSetChanged(); // 刷新适配器
				}
			});
			addView(linear, linearParams);
		}

	}

	class StypeLoading extends FrameLayout {

		String mTitle;
		Context mContext;
		ImageView mImage;

		int h, w;

		public StypeLoading(Context context, String title) {
			super(context);
			mTitle = title;
			mContext = context;
			initView();
		}

		@SuppressWarnings("deprecation")
		public void initView() {
			LinearLayout linearLayout = new LinearLayout(mContext);
			linearLayout.setGravity(Gravity.CENTER);

			linearLayout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.4f),
					(int) (mWidth * 0.4f));
			LinearLayout.LayoutParams contextParams = new LinearLayout.LayoutParams(
					Density.getInstence(mContext).dip2px(60), Density.getInstence(mContext).dip2px(60));
			// contextParams.leftMargin = ((int) (mWidth * 0.3f) -
			// Density.getInstence(mContext).dip2px(70)) / 2;
			// contextParams.bottomMargin =
			// Density.getInstence(mContext).dip2px(30);
			// contextParams.topMargin =
			// Density.getInstence(mContext).dip2px(10);
			// contextParams.leftMargin = ((int) (mWidth * 0.3f) -
			// Density.getInstence(mContext).dip2px(70)) / 2;
			CustomDrawables customs = new CustomDrawables(mContext, (int) (mWidth * 0.4f), (int) (mWidth * 0.4f),
					CustomDrawables.TYPE_DINGING_BACK);
			linearLayout.setBackgroundDrawable(customs.paintDrawable());

			linearLayout.getBackground().setAlpha(50);
			h = (int) (mWidth * 0.3 - Density.getInstence(mContext).dip2px(45));
			w = Density.getInstence(mContext).dip2px(60);
			h = w;

			mImage = new ImageView(mContext);
			mImage.setScaleType(ScaleType.CENTER_INSIDE);

			mImage.setImageDrawable(new Drawable() {
				int height = Density.getInstence(mContext).dip2px(0);
				int i = 0;
				int rx = w / 2;
				int ry = height + h / 2;
				int c = Density.getInstence(mContext).dip2px(3);
				int rectw = Density.getInstence(mContext).dip2px(5);

				@Override
				public void setColorFilter(ColorFilter colorFilter) {
				}

				@Override
				public void setAlpha(int alpha) {
				}

				@Override
				public int getOpacity() {
					return 0;
				}

				@Override
				public void draw(Canvas canvas) {
					Paint paint = new Paint();
					paint.setAntiAlias(true);
					// canvas.drawBitmap(bitmap, matrix, paint);
					RectF r = new RectF(rx - rectw / 2, height, rx + rectw / 2, height + h / 4f);
					paint.setColor(0xffb4b4b4);
					for (int i = 0; i < 12; i++) {
						canvas.save();
						canvas.rotate(30 * i, rx, ry);
						canvas.drawRoundRect(r, c, c, paint);
						canvas.restore();
					}

					if (i == 360) {
						i = 0;
					}
					paint.setColor(Color.WHITE);
					canvas.rotate(30 * i, rx, ry);
					canvas.drawRoundRect(r, c, c, paint);

					canvas.rotate(30, rx, ry);
					canvas.drawRoundRect(r, c, c, paint);
					paint.setColor(Color.WHITE);
					canvas.rotate(30, rx, ry);
					canvas.drawRoundRect(r, c, c, paint);

					try {
						Thread.sleep(60);
						invalidateSelf();
						i++;

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			});

			TextView text = new TextView(mContext);
			text.setText("");
			text.setGravity(Gravity.CENTER_HORIZONTAL);
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
			// linearLayout.addView(text,LinearLayout.LayoutParams.MATCH_PARENT,Density.getInstence(mContext).dip2px(35));
			linearLayout.addView(mImage, contextParams);
			addView(linearLayout, linearParams);
		}

	}

	class CustomDrawables {
		int mCheckColor = 0xffCCCCCC;
		// int mNoCheckColor = 0xff1e1e1e;
		int mNoCheckColor = Color.WHITE;
		int mWidth, mHeight;
		int mCorner = 8;
		int mFlag;
		Context mContext;
		public final static int TYPE_TITLE = 0;
		public final static int TYPE_LEFT_BUTTON = 1;
		public final static int TYPE_RIGHT_BUTTON = 2;
		public final static int TYPE_PROGRESS = 3;
		public final static int TYPE_BUTTON = 4;
		public final static int TYPE_ALERT = 5;
		public final static int TYPE_ALERT_L = 6;
		public final static int TYPE_ALERT_R = 7;
		public final static int TYPE_ALERT_C = 8;
		public final static int TYPE_DINGING_BACK = 9;

		public CustomDrawables(Context context, int mWidth, int mHeight, int mFlag) {
			super();
			mContext = context;
			this.mWidth = mWidth;
			this.mHeight = mHeight;
			this.mFlag = mFlag;
			mCorner = Density.getInstence(context).dip2px(8);
		}

		public void setFlag(int flag) {
			this.mFlag = flag;
		}

		public Drawable paintDrawable() {
			Drawable drawable = null;
			switch (mFlag) {
			case TYPE_TITLE:
				drawable = new TitleDrawable();
				break;
			case TYPE_RIGHT_BUTTON:
				drawable = new SelectorDrawable(TYPE_RIGHT_BUTTON).paint();
				break;
			case TYPE_LEFT_BUTTON:
				drawable = new SelectorDrawable(TYPE_LEFT_BUTTON).paint();
				break;
			case TYPE_BUTTON:
				drawable = new SelectorDrawable(TYPE_BUTTON).paint();
				break;
			case TYPE_PROGRESS:
				drawable = new TitleDrawable(TYPE_PROGRESS);
				break;
			case TYPE_ALERT:
				drawable = new TitleDrawable(TYPE_PROGRESS, 0);
				break;
			case TYPE_ALERT_L:
				drawable = new SelectorDrawable(TYPE_ALERT_L, 1).paint();
				break;
			case TYPE_ALERT_R:
				drawable = new SelectorDrawable(TYPE_ALERT_R, 1).paint();
				break;
			case TYPE_ALERT_C:
				drawable = new SelectorDrawable(TYPE_ALERT_C, 1).paint();
				break;
			case TYPE_DINGING_BACK:
				drawable = new TitleDrawable(TYPE_PROGRESS, "loading");
				break;

			default:
				break;
			}

			return drawable;

		}

		class TitleDrawable extends Drawable {
			int style;
			String backColor;

			public TitleDrawable() {
				super();

			}

			public TitleDrawable(int style) {
				super();
				this.style = style;

			}

			public TitleDrawable(int style, int color) {
				super();
				this.style = CustomDrawables.TYPE_PROGRESS;
				mNoCheckColor = Color.WHITE;
			}

			public TitleDrawable(int style, String color) {
				super();
				this.style = CustomDrawables.TYPE_PROGRESS;
				mNoCheckColor = 0x7f000000;
				backColor = color;
			}

			// 外部矩形弧度
			float[] outerR = new float[] { mCorner, mCorner, mCorner, mCorner, 0, 0, 0, 0 };
			// 内部矩形与外部矩形的距离
			RectF inset = new RectF(100, 100, 50, 50);
			// 内部矩形弧度
			float[] innerRadii = new float[] { 20, 20, 20, 20, 20, 20, 20, 20 };

			@Override
			public void draw(Canvas canvas) {
				if (style == CustomDrawables.TYPE_PROGRESS) {
					outerR = new float[] { mCorner, mCorner, mCorner, mCorner, mCorner, mCorner, mCorner, mCorner };
				}

				ShapeDrawable myShapeDrawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));

				myShapeDrawable.getPaint().setColor(mNoCheckColor);
				myShapeDrawable.setBounds(0, 0, mWidth, mHeight);
				myShapeDrawable.draw(canvas);

			}

			@Override
			public void setAlpha(int alpha) {

			}

			@Override
			public void setColorFilter(ColorFilter colorFilter) {

			}

			@Override
			public int getOpacity() {
				return 0;
			}

		}

		class ButtonDrawable extends Drawable {
			boolean ischeck = false;
			int direction = 0;
			float[] outerR = new float[] { mCorner, mCorner, mCorner, mCorner, 0, 0, 0, 0 };
			int color = 0;
			int width;

			public ButtonDrawable(boolean ischeck, int direction) {
				this.ischeck = ischeck;
				this.direction = direction;
			}

			public ButtonDrawable(boolean ischeck, int direction, int color) {
				this.ischeck = ischeck;
				this.direction = direction;
				if (color == 1) {
					changeColor();
				}
			}

			public void changeColor() {
				mCheckColor = Color.WHITE;
				mNoCheckColor = Color.RED;
			}

			@Override
			public void draw(Canvas canvas) {
				if (ischeck) {
					color = mCheckColor;
					switch (direction) {

					case TYPE_LEFT_BUTTON:
						width = mWidth / 2;
						outerR = new float[] { 0, 0, 0, 0, 0, 0, mCorner, mCorner };
						break;
					case TYPE_RIGHT_BUTTON:
						width = mWidth / 2;
						outerR = new float[] { 0, 0, 0, 0, mCorner, mCorner, 0, 0 };
						break;
					case TYPE_BUTTON:
						width = mWidth;
						outerR = new float[] { 0, 0, 0, 0, mCorner, mCorner, mCorner, mCorner };
						break;

					default:
						break;
					}
				} else {
					color = mNoCheckColor;
					switch (direction) {
					case TYPE_LEFT_BUTTON:
						width = mWidth / 2;
						outerR = new float[] { 0, 0, 0, 0, 0, 0, mCorner, mCorner };
						break;
					case TYPE_RIGHT_BUTTON:
						width = mWidth / 2;
						outerR = new float[] { 0, 0, 0, 0, mCorner, mCorner, 0, 0 };
						break;
					case TYPE_BUTTON:
						width = mWidth;
						outerR = new float[] { 0, 0, 0, 0, mCorner, mCorner, mCorner, mCorner };
						break;
					default:
						break;
					}
				}

				ShapeDrawable myShapeDrawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));
				myShapeDrawable.getPaint().setColor(color);
				myShapeDrawable.setBounds(0, 0, width, Density.getInstence(mContext).dip2px(50));
				myShapeDrawable.draw(canvas);

			}

			@Override
			public void setAlpha(int alpha) {

			}

			@Override
			public void setColorFilter(ColorFilter colorFilter) {

			}

			@Override
			public int getOpacity() {
				return 0;
			}

		}

		class SelectorDrawable {
			int direction;
			int flag;

			public SelectorDrawable(int direction) {
				super();
				this.direction = direction;
			}

			public SelectorDrawable(int direction, int flag) {
				super();
				this.direction = direction;
				this.flag = flag;
			}

			public Drawable paint() {
				StateListDrawable sld = new StateListDrawable();
				ButtonDrawable checkback, nocheckback;
				if (flag == 1) {
					checkback = new ButtonDrawable(true, direction, 1);
					nocheckback = new ButtonDrawable(false, direction, 1);
				} else {
					checkback = new ButtonDrawable(true, direction);
					nocheckback = new ButtonDrawable(false, direction);

					sld.addState(new int[] { -android.R.attr.state_pressed }, nocheckback);
					sld.addState(new int[] { android.R.attr.state_pressed }, checkback);
				}

				return sld;

			}

		}

	}

	class StyCancel extends FrameLayout {
		String[] text = { "continue", "cancel" };
		Context mContext;
		int mCorner;

		public StyCancel(Context context) {
			super(context);
			mContext = context;
			mCorner = Density.getInstence(mContext).dip2px(8);
			initView();
		}

		public void initView() {
			LinearLayout linear = new LinearLayout(mContext);
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) (mWidth * 0.7),
					LinearLayout.LayoutParams.WRAP_CONTENT | Gravity.BOTTOM);
			// linearParams.bottomMargin =
			// Density.getInstence(mContext).dip2px(10);
			linear.setOrientation(LinearLayout.VERTICAL);

			Button continueText = new Button(mContext);
			continueText.setGravity(Gravity.CENTER);
			continueText.setText(text[0]);
			StateListDrawable state = new StateListDrawable();
			state.addState(new int[] { android.R.attr.state_pressed }, new CanelDraw(0xffcccccc));
			state.addState(new int[] { -android.R.attr.state_pressed }, new CanelDraw(0x7f000000));
			continueText.setBackgroundDrawable(state);
			LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(getContext()).dip2px(35));
			// contentParams.bottomMargin =
			// Density.getInstence(mContext).dip2px(5);
			// linear.setGravity(Gravity.BOTTOM);
			// linear.setVisibility(View.INVISIBLE);
			View view = new View(mContext);
			view.setBackgroundColor(Color.TRANSPARENT);

			linear.addView(continueText, contentParams);
			linear.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(mContext).dip2px(5));
			Button cancelText = new Button(mContext);
			cancelText.setGravity(Gravity.CENTER);
			cancelText.setText(text[1]);
			StateListDrawable statecan = new StateListDrawable();
			statecan.addState(new int[] { android.R.attr.state_pressed }, new CanelDraw(0xffcccccc));
			statecan.addState(new int[] { -android.R.attr.state_pressed }, new CanelDraw(0x7f000000));
			Drawable draw = continueText.getBackground();
			cancelText.setBackgroundDrawable(statecan);
			linear.addView(cancelText, contentParams);
			View view1 = new View(mContext);
			view.setBackgroundColor(Color.TRANSPARENT);

			linear.addView(view1, LinearLayout.LayoutParams.MATCH_PARENT, Density.getInstence(mContext).dip2px(5));
			addView(linear, linearParams);
			if (sTypeface != null) {
				continueText.setTypeface(sTypeface);

				cancelText.setTypeface(sTypeface);
			}
		}

		class CanelDraw extends Drawable {
			int color;

			CanelDraw(int color) {
				this.color = color;
			}

			@Override
			public void setColorFilter(ColorFilter colorFilter) {

			}

			@Override
			public void setAlpha(int alpha) {

			}

			@Override
			public int getOpacity() {
				return 0;
			}

			@Override
			public void draw(Canvas canvas) {
				float[] jouterR = new float[] { mCorner, mCorner, mCorner, mCorner, mCorner, mCorner, mCorner,
						mCorner };
				ShapeDrawable myShapeDrawable = new ShapeDrawable(new RoundRectShape(jouterR, null, null));
				myShapeDrawable.getPaint().setColor(color);
				myShapeDrawable.setBounds(0, 0, (int) (mWidth * 0.7), Density.getInstence(getContext()).dip2px(35));
				myShapeDrawable.draw(canvas);
			}

		}

	}

	// 返回事件监听
	public interface ReturnResults {
		void result(Object o);
	}

	@SuppressWarnings("deprecation")
	public View radioItems() {

		LinearLayout contentlinear = new LinearLayout(getContext());
		contentlinear.setId(R.id.customDialog_container);
		TextView textview = new TextView(getContext());
		textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		textview.setTextColor(Color.BLACK);
		textview.setId(R.id.customDialog_textView);
		if (sTypeface != null) {
			textview.setTypeface(sTypeface);
		}
		textview.setPadding(Density.getInstence(getContext()).dip2px(10), 0, 0, 0);
		RadioButton radiobut = new RadioButton(getContext());
		radiobut.setFocusable(false);
		radiobut.setClickable(false);
		radiobut.setChecked(true);
		radiobut.setId(R.id.customDialog_radioButton);
		radiobut.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		StateListDrawable sld = new StateListDrawable();
		sld.addState(new int[] { -android.R.attr.state_checked }, new RadioDrawable(false));
		sld.addState(new int[] { android.R.attr.state_checked }, new RadioDrawable(true));
		radiobut.setBackgroundDrawable(sld);
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				Density.getInstence(getContext()).dip2px(50));
		textParams.leftMargin = Density.getInstence(getContext()).dip2px(20);
		textParams.weight = 1;
		contentlinear.addView(textview, textParams);
		contentlinear.addView(radiobut, Density.getInstence(getContext()).dip2px(70),
				Density.getInstence(getContext()).dip2px(50));

		return contentlinear;

	}

	class RadioDrawable extends Drawable {
		boolean mFlag;
		int ra, r, mSmallR;

		public RadioDrawable(boolean flag) {
			super();
			mFlag = flag;
			ra = Density.getInstence(getContext()).dip2px(25);
			r = Density.getInstence(getContext()).dip2px(10);
			mSmallR = Density.getInstence(getContext()).dip2px(5);
		}

		@Override
		public void draw(Canvas canvas) {
			// canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
			// Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			if (mFlag) {
				paint.setStyle(Paint.Style.STROKE);
				canvas.drawCircle(ra, ra, r, paint);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(ra, ra, mSmallR, paint);

			} else {
				paint.setStyle(Paint.Style.STROKE);
				canvas.drawCircle(ra, ra, r, paint);
			}
		}

		@Override
		public void setAlpha(int alpha) {

		}

		@Override
		public void setColorFilter(ColorFilter colorFilter) {

		}

		@Override
		public int getOpacity() {
			return 0;
		}

	}

	class ListViewAdapter extends BaseAdapter {
		private List<String> list;
		private int selectID;

		private OnMyCheckChangedListener mCheckChange;

		// 构造函数，用作初始化各项数据
		public ListViewAdapter(Context context, List<String> list) {
			this.list = list;
		}

		// 获取ListView的item总数
		public int getCount() {
			return list.size();
		}

		// 获取ListView的item
		public Object getItem(int position) {
			return getItem(position);
		}

		// 获取ListView的item的ID
		public long getItemId(int position) {
			return position;
		}

		// 自定义的选中方法
		public void setSelectID(int position) {
			selectID = position;
		}

		// 获取item的视图及其中含有的操作
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewCache viewCache;

			/**
			 * 这个是网上流行的适配器缓存View写法(软引用原理)，就不多说了。
			 */
			if (convertView == null) {
				viewCache = new ViewCache();
				// convertView =
				// LayoutInflater.from(context).inflate(R.layout.list_item,
				// null);
				convertView = radioItems();
				viewCache.linearLayout = (LinearLayout) convertView.findViewById(R.id.customDialog_container);

				viewCache.itemName = (TextView) convertView.findViewById(R.id.customDialog_textView);
				viewCache.radioBtn = (RadioButton) convertView.findViewById(R.id.customDialog_radioButton);

				convertView.setTag(viewCache);
			} else {
				viewCache = (ViewCache) convertView.getTag();
			}

			viewCache.itemName.setText(list.get(position));

			// // 核心方法，判断单选按钮被按下的位置与之前的位置是否相等，然后做相应的操作。
			if (selectID == position) {
				// // viewCache.linearLayout.setBackgroundColor(Color.BLUE);
				viewCache.radioBtn.setChecked(true);
			} else {
				// // viewCache.linearLayout.setBackgroundColor(0);
				viewCache.radioBtn.setChecked(false);
			}
			StateListDrawable state = new StateListDrawable();
			// state.addState(new int[] { -android.R.attr.state_pressed }, new
			// ColorDrawable(0x000000000));
			// state.addState(new int[] { android.R.attr.state_pressed }, new
			// ColorDrawable(0xddddddd));
			// viewCache.linearLayout.setBackgroundDrawable(state);
			// 单选按钮的点击事件监听
			// 单选按钮的点击事件监听
			// viewCache.radioBtn.setonc

			return convertView;
		}

		// 回调函数，很类似OnClickListener吧，呵呵
		public void setOncheckChanged(OnMyCheckChangedListener l) {
			mCheckChange = l;
		}

		// 缓存类
		class ViewCache {
			LinearLayout linearLayout;
			TextView itemID, itemName;
			RadioButton radioBtn;
		}
	}

	static class Density {

		private static Density sDensity;

		public static final float DEFAULT_SCALE = 2;

		public static Density getInstence(Context context) {
			if (sDensity == null) {
				sDensity = new Density(context);
			}
			return sDensity;
		}

		private DisplayMetrics mDM;
		private float scale;
		private float fontScale;
		private int mStatusBarHeight;

		public Density(Context context) {
			mDM = context.getResources().getDisplayMetrics();
			scale = mDM.density;
			fontScale = mDM.scaledDensity;
			mStatusBarHeight = getStatusBarHeight(context);
		}

		private int getStatusBarHeight(Context context) {
			int result = 0;
			int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = context.getResources().getDimensionPixelSize(resourceId);
			}
			return result;
		}

		public int getStatusBarHeight() {
			return mStatusBarHeight;
		}

		public int getScreenWidth() {
			return mDM.widthPixels;
		}

		public int getScreenHeight() {
			return mDM.heightPixels;
		}

		public int dip2px(float dpValue) {
			return (int) (dpValue * scale + 0.5f);
		}

		public int px2dip(float pxValue) {
			return (int) (pxValue / scale + 0.5f);
		}

		public int px2sp(float pxValue) {
			return (int) (pxValue / fontScale + 0.5f);
		}

		public int sp2px(float spValue) {
			return (int) (spValue * fontScale + 0.5f);
		}
	}

	// 单选框选中监听
	public interface OnMyCheckChangedListener {
		void setSelectID(int selectID);
	}

}
