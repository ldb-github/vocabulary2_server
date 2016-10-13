package com.ldb.vocabulary2.server.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ldb.util.DateUtils;
import com.ldb.util.FileOperation;
import com.ldb.vocabulary2.server.service.ICategoryService;
import com.ldb.vocabulary2.server.dao.ICategoryDao;
import com.ldb.vocabulary2.server.dao.ICommonDao;
import com.ldb.vocabulary2.server.dao.impl.CategoryDao;
import com.ldb.vocabulary2.server.dao.impl.CommonDao;
import com.ldb.vocabulary2.server.domain.Category;
import com.ldb.vocabulary2.server.domain.CommunicationContract;
import com.ldb.vocabulary2.server.domain.Constants;
import com.ldb.vocabulary2.server.domain.Vocabulary;

public class CategoryService implements ICategoryService {

	private static final int PAGE_SIZE = 50;
	private static final int PAGE_SIZE_VOCABULARY = 50;
	private static Object lock = new Object();
	private ICommonDao commonDao = new CommonDao();

	private ICategoryDao categoryDao = new CategoryDao();

	@Override
	public String getCategoryList(int page, String sort, String sortType) {
		// TODO 实现从数据库获取词汇类别列表

		int code = CommunicationContract.VALUE_CODE_UNKNOWNERROR;
		String message = null;
		JSONObject resultJson = new JSONObject();

		String[] sortArray = null;
		String[] sortTypeArray = null;
		if (sort != null && !sort.trim().isEmpty()) {
			sortArray = sort.split(",");
		}
		if (sortType != null && !sortType.trim().isEmpty()) {
			sortTypeArray = sortType.split(",");
		}

		boolean isContinue = true;
		if (sortArray != null) {
			if (sortTypeArray == null || sortArray.length != sortTypeArray.length) {
				isContinue = false;
				code = CommunicationContract.VALUE_CODE_ERROR;
				message = "排序方式与排序类型不一致";
			}
		}

		if (page < 1) {
			page = 1;
		}

		// 默认的排序方式
		if (sortArray == null) {
			sortArray = new String[1];
			sortArray[0] = "f";
			sortTypeArray = new String[1];
			sortTypeArray[0] = "d";
		}

		if (isContinue) {
			try {
				List<Category> categories = categoryDao.getCategoryList(page, PAGE_SIZE, sortArray, sortTypeArray);
				code = CommunicationContract.VALUE_CODE_OK;
				message = "类别列表获取成功";
				if (!categories.isEmpty()) {
					JSONArray list = new JSONArray();
					JSONObject listItem;
					for (Category category : categories) {
						listItem = getJsonObjectFrom(category); //new JSONObject();
//						listItem.put(CommunicationContract.KEY_CATEGORY_ID, category.getId());
//						listItem.put(CommunicationContract.KEY_CATEGORY_NAME, category.getName());
//						listItem.put(CommunicationContract.KEY_CATEGORY_IMAGE, category.getImage());
//						listItem.put(CommunicationContract.KEY_CATEGORY_IMAGE_REMOTE, category.getImageRemote());
//						listItem.put(CommunicationContract.KEY_CATEGORY_FAVORITE_COUNT, category.getFavoriteCount());
//						listItem.put(CommunicationContract.KEY_CATEGORY_WORD_COUNT, category.getWordCount());
//						listItem.put(CommunicationContract.KEY_CATEGORY_CREATER, category.getUsername());
//						listItem.put(CommunicationContract.KEY_CATEGORY_CREATE_TIME, category.getCreateTime());
//						listItem.put(CommunicationContract.KEY_CATEGORY_TRANSLATION, category.getTranslation());
						list.put(listItem);
					}
					resultJson.put(CommunicationContract.KEY_CATEGORY_LIST, list);
				}

			} catch (SQLException e) {
				code = CommunicationContract.VALUE_CODE_DBERROR;
				message = "获取词汇分类列表失败：数据库请求错误";
			}
		}

		resultJson.put(CommunicationContract.KEY_CODE, code);
		resultJson.put(CommunicationContract.KEY_MESSAGE, message);

		return resultJson.toString();
	}

	@Override
	public String getCategoryItemList(String categoryId, int page) {
		int code = CommunicationContract.VALUE_CODE_UNKNOWNERROR;
		String message = null;
		
		if (page < 1) {
			page = 1;
		}

		JSONObject resultJson = new JSONObject();
		resultJson.put(CommunicationContract.KEY_CODE, CommunicationContract.VALUE_CODE_OK);
		resultJson.put(CommunicationContract.KEY_MESSAGE, "词汇列表获取成功");

		resultJson.put(CommunicationContract.KEY_CATEGORY_ID, categoryId);

		try {
			List<Vocabulary> vocabularies = 
					categoryDao.getVocabularyList(categoryId, page, PAGE_SIZE_VOCABULARY);
			code = CommunicationContract.VALUE_CODE_OK;
			message = "词汇列表获取成功";
			if(!vocabularies.isEmpty()){
				JSONArray list = new JSONArray();
				JSONObject listItem;
				for(Vocabulary vocabulary : vocabularies){
					listItem = new JSONObject();
					listItem.put(CommunicationContract.KEY_VOCABULARY_ID, vocabulary.getId());
					listItem.put(CommunicationContract.KEY_VOCABULARY_NAME, vocabulary.getName());
					listItem.put(CommunicationContract.KEY_VOCABULARY_IMAGE, vocabulary.getImage());
					listItem.put(CommunicationContract.KEY_VOCABULARY_IMAGE_REMOTE, vocabulary.getImageRemote());
					listItem.put(CommunicationContract.KEY_VOCABULARY_CREATER, vocabulary.getUsername());
					listItem.put(CommunicationContract.KEY_VOCABULARY_CREATE_TIME, vocabulary.getCreateTime());
					listItem.put(CommunicationContract.KEY_VOCABULARY_TRANSLATION, vocabulary.getTranslation());
					list.put(listItem);
				}
				resultJson.put(CommunicationContract.KEY_VOCABULARY_LIST, list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			code = CommunicationContract.VALUE_CODE_DBERROR;
			message = "获取词汇列表失败：数据库请求错误";
		}

		resultJson.put(CommunicationContract.KEY_CODE, code);
		resultJson.put(CommunicationContract.KEY_MESSAGE, message);
		
		return resultJson.toString();

	}

	@Override
	public void getImage(String categoryId, String imageName, OutputStream out) {
		FileInputStream fileIn = null;
		FileChannel in = null;
		BufferedOutputStream bufferedOut = null;
		try {
			fileIn = new FileInputStream(Constants.UPLOAD_IMAGE_DIR + "\\" + categoryId + "\\" + imageName);
			in = fileIn.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			bufferedOut = new BufferedOutputStream(out);
			while (in.read(buffer) != -1) {
				buffer.flip();
				bufferedOut.write(buffer.array());
				buffer.clear();
			}
			// 记得flush，开始漏了这个，客户端解析不到数据！！
			bufferedOut.flush();

		} catch (IOException e) {
			// TODO 啥都不做
		} finally {
			if (bufferedOut != null) {
				try {
					bufferedOut.close();
				} catch (IOException e) {
					// TODO 啥都不做
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO 啥都不做
				}
			}
			if (fileIn != null) {
				try {
					fileIn.close();
				} catch (IOException e) {
					// TODO 啥都不做
				}
			}
		}
	}

	// private void getImage2(String url, OutputStream out) throws IOException {
	// // 想仿照
	// //
	// https://farm9.staticflickr.com/8041/28769655083_9954c93dd3_m.jpg，返回一个<img..>标签
	// // ，没有成功
	// out.write(("<img src='" + url + "' />").getBytes());
	// }

	@Override
	public String addCategory(Category category) {
		boolean isContinue = true;
		int code = CommunicationContract.VALUE_CODE_UNKNOWNERROR;
		String message = null;
		JSONObject resultJson = new JSONObject();

		// 提交的词汇类别有误，在servlet层解析失败
		if (category == null) {
			isContinue = false;
			code = CommunicationContract.VALUE_CODE_PARAMSERROR;
			message = "词汇类别提交错误";
		}

		String categoryId = null;
		// 获取词汇类别的id
		if (isContinue) {
			synchronized (lock) {
				try {
					categoryId = commonDao.getParameterValueForUpdate(Constants.PARAMETER_CATEGORY_ID, "1");
				} catch (SQLException e) {
					// TODO 添加失败处理
					isContinue = false;
					code = CommunicationContract.VALUE_CODE_DBERROR;
					message = "数据库请求错误：获取类别id参数值错误";
				}

			}
		}
		// 处理category
		if (isContinue) {
			category.setId(categoryId);
			category.setCreateTime(new Timestamp(DateUtils.getCurrentDate().getTime()));
			category.setFavoriteCount(0);
			category.setWordCount(0);
			// TODO category.setLanguage("en");
			// 生成图片的url
			if (category.getImage() != null && !category.getImage().trim().equals("")) {
				// TODO 移动图片 从零时目录到类别目录
				String path = Constants.UPLOAD_IMAGE_DIR + "\\" + category.getId();
				File file = new File(path);
				// 判断上传文件的保存目录是否存在，不存在则创建目录
				if (!file.exists() && !file.isDirectory()) {
					file.mkdirs();
				}
				try {
					String newImageName = FileOperation.fileMove(
							Constants.UPLOAD_IMAGE_DIR_TEMP + "\\" + category.getImage(),
							Constants.UPLOAD_IMAGE_DIR + "\\" + category.getId() + "\\" + category.getImage(), false);
					category.setImage(newImageName);

					// 之前category保存的是image的名称，这里调整为完整的访问路径
					category.setImage(Constants.IMAGE_URL_PREF + category.getId() + "/" + category.getImage());
				} catch (IOException e) {
					// TODO 文件移动错误
					isContinue = false;
					code = CommunicationContract.VALUE_CODE_ERROR;
					message = "图片移动错误";
				}
			}
		}
		// 往数据库添加词汇类别
		if (isContinue) {
			try {
				categoryDao.addCategory(category);
				JSONObject categoryJson = getJsonObjectFrom(category);
				resultJson.put(CommunicationContract.KEY_CATEGORY_LIST, categoryJson);
			} catch (SQLException e) {
				// TODO 类别添加错误
				e.printStackTrace();
				isContinue = false;
				code = CommunicationContract.VALUE_CODE_DBERROR;
				message = "数据库请求错误：词汇类别添加失败";
			}
		}
		// 成功
		if (isContinue) {
			code = CommunicationContract.VALUE_CODE_OK;
			message = "词汇类别添加成功";
		}

		resultJson.put(CommunicationContract.KEY_CODE, code);
		resultJson.put(CommunicationContract.KEY_MESSAGE, message);

		return resultJson.toString();
	}

	public String addVocabulary(Vocabulary vocabulary) {
		boolean isContinue = true;
		int code = CommunicationContract.VALUE_CODE_UNKNOWNERROR;
		String message = null;
		JSONObject resultJson = new JSONObject();

		// 提交的词汇类别有误，在servlet层解析失败
		if (vocabulary == null || vocabulary.getName() == null || vocabulary.getName().trim().isEmpty()) {
			isContinue = false;
			code = CommunicationContract.VALUE_CODE_PARAMSERROR;
			message = "词汇提交错误";
		}

		String vocabularyId = null;
		// 获取词汇类别的id
		if (isContinue) {
			synchronized (lock) {
				try {
					vocabularyId = commonDao.getParameterValueForUpdate(Constants.PARAMETER_VOCABULARY_ID, "1");
				} catch (SQLException e) {
					// TODO 添加失败处理
					isContinue = false;
					code = CommunicationContract.VALUE_CODE_DBERROR;
					message = "数据库请求错误：获取词汇id参数值错误";
				}

			}
		}
		// 处理vocabulary
		if (isContinue) {
			String cId = vocabulary.getCId();
			vocabulary.setId(vocabularyId);
			vocabulary.setCreateTime(new Timestamp(DateUtils.getCurrentDate().getTime()));
			// TODO vocabulary.setLanguage("en");
			// 生成图片的url
			if (vocabulary.getImage() != null && !vocabulary.getImage().trim().equals("")) {
				// TODO 移动图片 从零时目录到类别目录
				String path = Constants.UPLOAD_IMAGE_DIR + "\\" + vocabulary.getId();
				File file = new File(path);
				// 判断上传文件的保存目录是否存在，不存在则创建目录
				if (!file.exists() && !file.isDirectory()) {
					file.mkdirs();
				}
				try {
					String newImageName = FileOperation.fileMove(
							Constants.UPLOAD_IMAGE_DIR_TEMP + "\\" + vocabulary.getImage(),
							Constants.UPLOAD_IMAGE_DIR + "\\" + cId + "\\" + vocabulary.getImage(), false);
					vocabulary.setImage(newImageName);

					// 之前vocabulary保存的是image的名称，这里调整为完整的访问路径
					vocabulary.setImage(Constants.IMAGE_URL_PREF + cId + "/" + vocabulary.getImage());
				} catch (IOException e) {
					// TODO 文件移动错误
					isContinue = false;
					code = CommunicationContract.VALUE_CODE_ERROR;
					message = "图片移动错误";
				}
			}
		}
		// 往数据库添加词汇类别
		if (isContinue) {
			try {
				// 添加词汇
				categoryDao.addVocabulary(vocabulary);
				JSONObject vocabularyJson = getJsonObjectFrom(vocabulary);
				resultJson.put(CommunicationContract.KEY_VOCABULARY_LIST, vocabularyJson);
			} catch (SQLException e) {
				// TODO 类别添加错误
				e.printStackTrace();
				isContinue = false;
				code = CommunicationContract.VALUE_CODE_DBERROR;
				message = "数据库请求错误：词汇添加失败";
			}
		}
		// 成功
		if (isContinue) {
			code = CommunicationContract.VALUE_CODE_OK;
			message = "词汇添加成功";
		}

		resultJson.put(CommunicationContract.KEY_CODE, code);
		resultJson.put(CommunicationContract.KEY_MESSAGE, message);

		return resultJson.toString();
	}
	
	private JSONObject getJsonObjectFrom(Category category){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CommunicationContract.KEY_CATEGORY_ID, category.getId());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_NAME, category.getName());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_IMAGE, category.getImage());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_IMAGE_REMOTE, category.getImageRemote());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_FAVORITE_COUNT, category.getFavoriteCount());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_WORD_COUNT, category.getWordCount());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_LANGUAGE, category.getLanguage());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_CREATER, category.getUsername());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_CREATE_TIME, category.getCreateTime());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_TRANSLATION, category.getTranslation());
		
		return jsonObject;
	}
	
	private JSONObject getJsonObjectFrom(Vocabulary vocabulary){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_ID, vocabulary.getId());
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_NAME, vocabulary.getName());
		jsonObject.put(CommunicationContract.KEY_CATEGORY_ID, vocabulary.getCId());
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_IMAGE, vocabulary.getImage());
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_IMAGE_REMOTE, vocabulary.getImageRemote());
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_LANGUAGE, vocabulary.getLanguage());
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_CREATER, vocabulary.getUsername());
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_CREATE_TIME, vocabulary.getCreateTime());
		jsonObject.put(CommunicationContract.KEY_VOCABULARY_TRANSLATION, vocabulary.getTranslation());
		
		return jsonObject;
	}

}
