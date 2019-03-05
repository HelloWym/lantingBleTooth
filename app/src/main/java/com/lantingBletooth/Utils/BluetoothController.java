package com.lantingBletooth.Utils;

import java.util.Iterator;
import java.util.List;

import com.lantingBletooth.Application.BleToothApplication;
import com.lantingBletooth.Entity.EntityDevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import static com.lantingBletooth.Utils.MyUtils.hexStringToByteArray;

/**
 * ����������
 */
public class BluetoothController {
	private String deviceAddress;
	private String deviceName;

	private BluetoothAdapter bleAdapter;
	private Handler serviceHandler;//������

	static BluetoothGatt bleGatt;// ����
	static BluetoothGattCharacteristic bleGattCharacteristic;
	static BluetoothGattCharacteristic getGattCharacteristic;//������
	static BluetoothGattCharacteristic writeGattCharacteristic;//д����

	/**
	 * ����ģʽ
	 */
	private static BluetoothController instance = null;

	private BluetoothController() {
	}

	public static BluetoothController getInstance() {
		if (instance == null)
			instance = new BluetoothController();
		return instance;
	}

	/**
	 * ��ʼ������
	 * 
	 * @return
	 */
	public boolean initBLE() {
		// ��鵱ǰ�ֻ��Ƿ�֧��ble ����,�����֧���˳�����
		// App.app���ܻᱨ���嵥�ļ��в�Ҫ��������application
		if (!BleToothApplication.instance().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			return false;
		}
		// ��ʼ�� Bluetooth adapter, ͨ�������������õ�һ���ο�����������(API����������android4.3�����ϰ汾)
		final BluetoothManager bluetoothManager = (BluetoothManager) BleToothApplication.instance()
				.getSystemService(Context.BLUETOOTH_SERVICE);
		bleAdapter = bluetoothManager.getAdapter();
		// ����豸���Ƿ�֧������
		if (bleAdapter == null)
			return false;
		else
			return true;
	}

	/**
	 * ���÷����¼�������
	 * 
	 * @return
	 */
	public void setServiceHandler(Handler handler) {
		// handler��ʼ����service�У������߼��ͽ���Ĺ�ͨ
		serviceHandler = handler;
	}

	/**
	 * ���������ص�
	 */
	BluetoothAdapter.LeScanCallback bleScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int arg1, byte[] arg2) {
			// device�������������豸
			String name = device.getName();
			if (name == null)
				return;
			if (BluetoothController.this.serviceHandler != null
					&& !name.isEmpty()) {
				Message msg = new Message();
				msg.what = ConstantUtils.WM_UPDATE_BLE_LIST;
				msg.obj = device;
				BluetoothController.this.serviceHandler.sendMessage(msg);
			}
		}
	};

	/**
	 * ��ʼɨ������
	 */
	public void startScanBLE() {
		bleAdapter.startLeScan(bleScanCallback);
		if (serviceHandler != null)
			serviceHandler.sendEmptyMessageDelayed(
					ConstantUtils.WM_STOP_SCAN_BLE, 5000);
	}

	/**
	 * ֹͣɨ�������豸
	 */
	public void stopScanBLE() {
		bleAdapter.stopLeScan(bleScanCallback);
	}

	/**
	 * �Ƿ�������
	 * 
	 * @return
	 */
	public boolean isBleOpen() {
		return bleAdapter.isEnabled();
	}

	/**
	 * ���������豸
	 * 
	 * @param device
	 *            �����ӵ��豸
	 */
	public void connect(EntityDevice device) {
		deviceAddress = device.getAddress();
		deviceName = device.getName();
		BluetoothDevice localBluetoothDevice = bleAdapter
				.getRemoteDevice(device.getAddress());
		if (bleGatt != null) {
			bleGatt.disconnect();
			bleGatt.close();
			bleGatt = null;
		}
		bleGatt = localBluetoothDevice.connectGatt(BleToothApplication.instance(), false,
				bleGattCallback);
	}

	/**
	 * �ж��Ƿ���������
	 * @return
	 */
	public boolean isConnect(){
		if (writeGattCharacteristic == null)
			return false;
		if(bleGatt==null)
			return false;
		else return true;
	}
	/**
	 * �Ͽ������豸
	 */
	public void stopConnect(){
		if(bleGatt!=null){
			bleGatt.close();
			bleGatt = null;
		}
	}

	/**
	 * ������ͨ�Żص�
	 */
	public BluetoothGattCallback bleGattCallback = new BluetoothGattCallback() {
		/**
		 * �յ���Ϣ
		 */
		public void onCharacteristicChanged(BluetoothGatt paramAnonymousBluetoothGatt,
											BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic) {

			byte[] arrayOfByte = paramAnonymousBluetoothGattCharacteristic
					.getValue();
			if (BluetoothController.this.serviceHandler != null) {
				Message msg = new Message();
				msg.what = ConstantUtils.WM_RECEIVE_MSG_FROM_BLE;
				// byte����ת��Ϊʮ�������ַ���
				msg.obj = ConvertUtils.getInstance().bytesToHexString(
						arrayOfByte);
				BluetoothController.this.serviceHandler.sendMessage(msg);
			}
			// Ҳ�����ȴ�ӡ��������
			Log.i("TEST",
					ConvertUtils.getInstance().bytesToHexString(arrayOfByte));
		}

		public void onCharacteristicRead(
				BluetoothGatt paramAnonymousBluetoothGatt,
				BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic,
				int paramAnonymousInt) {
		}

		public void onCharacteristicWrite(
				BluetoothGatt paramAnonymousBluetoothGatt,
				BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic,
				int paramAnonymousInt) {
		}

		/**
		 * ����״̬�ı�
		 */
		public void onConnectionStateChange(
				BluetoothGatt paramAnonymousBluetoothGatt, int oldStatus,
				int newStatus) {
			if (newStatus == BluetoothProfile.STATE_CONNECTED)// ������״̬���������ӳɹ�
			{
				Message msg = new Message();
				msg.what = ConstantUtils.WM_BLE_CONNECTED_STATE_CHANGE;
				Bundle bundle = new Bundle();
				bundle.putString("address", deviceAddress);
				bundle.putString("name", deviceName);
				msg.obj = bundle;
				serviceHandler.sendMessage(msg);
				paramAnonymousBluetoothGatt.discoverServices();
//				byte[] bytes = MyUtils.hexStringToByteArray("ABEF00000001010000FFFCFFFF");
//                write(bytes);
				// ���ӵ���������ҿ��Զ�д�ķ��������кܶ����
				return;
			}
			if (newStatus == 0)// �Ͽ����ӻ�δ���ӳɹ�
			{
				serviceHandler.sendEmptyMessage(ConstantUtils.WM_STOP_CONNECT);
				return;
			}
			paramAnonymousBluetoothGatt.disconnect();
			paramAnonymousBluetoothGatt.close();
			return;
		}

		public void onDescriptorRead(BluetoothGatt paramAnonymousBluetoothGatt,
				BluetoothGattDescriptor paramAnonymousBluetoothGattDescriptor,
				int paramAnonymousInt) {
		}

		public void onDescriptorWrite(
				BluetoothGatt paramAnonymousBluetoothGatt,
				BluetoothGattDescriptor paramAnonymousBluetoothGattDescriptor,
				int paramAnonymousInt) {
		}

		public void onReadRemoteRssi(BluetoothGatt paramAnonymousBluetoothGatt,
				int paramAnonymousInt1, int paramAnonymousInt2) {
		}

		public void onReliableWriteCompleted(
				BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt) {
		}
		//���ַ���ص�
		public void onServicesDiscovered(
				BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt) {
			BluetoothController.this.findService(paramAnonymousBluetoothGatt
					.getServices());
		}

	};

	/**
	 * ��������
	 * 
	 * @param byteArray
	 * @return
	 */
	public boolean write(byte byteArray[]) {
		if (writeGattCharacteristic == null)
			return false;
		if (bleGatt == null)
			return false;
		writeGattCharacteristic.setValue(byteArray);
		return bleGatt.writeCharacteristic(writeGattCharacteristic);
	}

	/**
	 * ��������
	 * 
	 * @param
	 * @return
	 */
	public boolean write(String str) {
		if (writeGattCharacteristic == null)
			return false;
		if (bleGatt == null)
			return false;
		writeGattCharacteristic.setValue(str);
		return bleGatt.writeCharacteristic(writeGattCharacteristic);
	}

	/**
	 * ��������
	 * 
	 * @param paramList
	 */
	public void findService(List<BluetoothGattService> paramList) {
        Log.e("findservice","findservice");
		Iterator localIterator1 = paramList.iterator();
		while (localIterator1.hasNext()) {
			BluetoothGattService localBluetoothGattService = (BluetoothGattService) localIterator1
					.next();
			if (localBluetoothGattService.getUuid().toString()
					.equalsIgnoreCase(ConstantUtils.UUID_SERVER)) {
				List localList = localBluetoothGattService.getCharacteristics();
				Iterator localIterator2 = localList.iterator();
				while (localIterator2.hasNext()) {
					BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic) localIterator2
							.next();
					if (localBluetoothGattCharacteristic.getUuid().toString()
							.equalsIgnoreCase(ConstantUtils.UUID_NOTIFY)) {
						getGattCharacteristic = localBluetoothGattCharacteristic;
					}
					if (localBluetoothGattCharacteristic.getUuid().toString()
							.equalsIgnoreCase(ConstantUtils.UUID_WRITE)) {
						writeGattCharacteristic = localBluetoothGattCharacteristic;
					}
				}
				break;
			}
		}
		Boolean b = bleGatt.setCharacteristicNotification(getGattCharacteristic, true);
		//���������ȷ�һ������
		byte[] bytes = hexStringToByteArray("ABEF00000001010000FFFCFFFF");
		write(bytes);
	}

}
