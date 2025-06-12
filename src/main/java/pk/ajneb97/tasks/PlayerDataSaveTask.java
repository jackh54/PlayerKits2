package pk.ajneb97.tasks;

import pk.ajneb97.PlayerKits2;
import pk.ajneb97.utils.SchedulerUtils;

public class PlayerDataSaveTask {

	private PlayerKits2 plugin;
	private boolean end;
	private Object task;
	
	public PlayerDataSaveTask(PlayerKits2 plugin) {
		this.plugin = plugin;
		this.end = false;
	}
	
	public void end() {
		end = true;
		if (task != null) {
			SchedulerUtils.cancelTask(task);
		}
	}
	
	public void start(int seconds) {
		long ticks = seconds * 20L;
		
		task = SchedulerUtils.runTaskTimerAsynchronously(plugin, () -> {
			if (end) {
				SchedulerUtils.cancelTask(task);
			} else {
				execute();
			}
		}, 0L, ticks);
	}
	
	public void execute() {
		plugin.getConfigsManager().getPlayersConfigManager().saveConfigs();
	}
}
