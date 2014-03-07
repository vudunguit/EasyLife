/*
 * Copyleft 2012 - Peer internet solutions 
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package covisoft.android.AR.mgr.datasource;

import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

import covisoft.android.AR.MixContext;
import covisoft.android.AR.data.DataSource;
import covisoft.android.AR.data.DataSourceStorage;
import covisoft.android.AR.mgr.downloader.DownloadRequest;
import covisoft.android.tab3_Home.Activity_Home_List;

class DataSourceMgrImpl implements DataSourceManager {

	private final ConcurrentLinkedQueue<DataSource> allDataSources = new ConcurrentLinkedQueue<DataSource>();

	private final MixContext ctx;

	public DataSourceMgrImpl(MixContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public boolean isAtLeastOneDatasourceSelected() {
		boolean atLeastOneDatasourceSelected = false;
		for (DataSource ds : this.allDataSources) {
			if (ds.getEnabled())
				atLeastOneDatasourceSelected = true;
		}
		return atLeastOneDatasourceSelected;
	}

	public void setAllDataSourcesforLauncher(DataSource datasource) {
		this.allDataSources.clear(); // TODO WHY? CLEAN ALL
		this.allDataSources.add(datasource);
	}

	public void refreshDataSources() {
		this.allDataSources.clear();

		DataSourceStorage.getInstance(ctx).fillDefaultDataSources();

		DataSourceStorage.getInstance().getSize();

		String fields[] = DataSourceStorage.getInstance().getFields(0);
		this.allDataSources.add(new DataSource(fields[0], fields[1], fields[2], fields[3], fields[4]));
		this.allDataSources.add(new DataSource("EZLIFE", Activity_Home_List.url, fields[2], fields[3], fields[4]));
	}

	public void requestDataFromAllActiveDataSource(double lat, double lon, double alt, float radius) {
		for (DataSource ds : allDataSources) {
			/*
			 * when type is OpenStreetMap iterate the URL list and for selected
			 * URL send data request
			 */
			if (ds.getEnabled()) {
				requestData(ds, lat, lon, alt, radius, Locale.getDefault().getLanguage());
			}
		}

	}

	private void requestData(DataSource datasource, double lat, double lon, double alt, float radius, String locale) {
		DownloadRequest request = new DownloadRequest(datasource, datasource.createRequestParams(lat, lon, alt, radius, locale));
		ctx.getDownloadManager().submitJob(request);

	}

}
