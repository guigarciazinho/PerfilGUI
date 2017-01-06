package me.guigarciazinho.Perfil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Principal extends JavaPlugin implements Listener {
	public static File configFile;
	public static YamlConfiguration configPerfil;
	private static Principal instance;

	@Override
	public void onEnable() {
		instance = this;
		System.out.println("==================");
		System.out.println("Plugin Perfil foi habilitado");
		System.out.println("Criado por kami, guigarciazinho");
		System.out.println("==================");
		Bukkit.getPluginManager().registerEvents(this, this);
		saveDefaultConfig();

		configFile = new File(getDataFolder(), "Perfis.yml");
		if (!configFile.exists() && getConfig().getBoolean("Use MySQL") == false) {

			saveResource("Perfis.yml", false);
		} else if (getConfig().getBoolean("Use MySQL") == true) {
			bd BD = new bd();
			BD.criar();
		}

		configPerfil = YamlConfiguration.loadConfiguration(configFile);
		try {
			configPerfil.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		System.out.println("==================");
		System.out.println("Plugin Perfil foi desabilitado");
		System.out.println("Criado por kami, guigarciazinho");
		System.out.println("==================");
		HandlerList.unregisterAll();
	}

	public static Principal getInstance() {
		return instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		bd BD = new bd();
		String prefixo = getConfig().getString("Prefixo").replace("&", "§");
		Player p = (Player) sender;
		Player t = Bukkit.getServer().getPlayer(args[0]);

		// Criando perfil -------------
		if (sender instanceof Player) {
			if (command.getName().equalsIgnoreCase("perfil")) {

				if (args.length > 0) {
					if ("criar".equalsIgnoreCase(args[0])) {
						if (p.hasPermission("perfil.criar")) {
							if (getConfig().getBoolean("Use MySQL") == true) {
								if (BD.criaPerfil(p.getUniqueId(), p.getName()) == false) {
									p.sendMessage(prefixo + getConfig().getString("Ja criado").replace("&", "§"));
									return false;
								} else {
									BD.criaPerfil(p.getUniqueId(), p.getName());
									p.sendMessage(prefixo + getConfig().getString("Criado").replace("&", "§"));
									return true;
								}
							}
							if (getConfig().getBoolean("Use MySQL") == false) {
								// NAO ESTA USANDO MYSQL ------ INICIO
								if (configPerfil.contains(p.getUniqueId() + ".Perfil")) {
									p.sendMessage(prefixo + getConfig().getString("Ja criado").replace("&", "§"));
									return true;
								} else if (!configPerfil.contains(p.getUniqueId() + ".Perfil")) {
									configPerfil.set(p.getUniqueId() + ".Perfil" + ".Nome", p.getName());
									p.sendMessage(prefixo + getConfig().getString("Criado").replace("&", "§"));
									try {
										configPerfil.save(configFile);
									} catch (IOException d) {
										d.printStackTrace();
									}
									return true;
								}
								// NAO ESTA USANDO MYSQL ------ FIM
							}

						}
					}
				}

				// Criando perfil --------------------- END

				// Adicionando Skype --------------------------
				if ("skype".equalsIgnoreCase(args[0])) {
					if (configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						if (p.hasPermission("perfil.criar")) {
							if (args.length >= 2) {
								String skype = "";
								for (int i = 1; i < args.length; i++) {
									skype = skype + args[i] + " ";
								}
								if (getConfig().getBoolean("Use MySQL") == true) {
									if (BD.checar(p.getUniqueId()) == true) {
										BD.add("skype", skype, p.getUniqueId());
										p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
												+ args[0].toLowerCase());
										return true;
									} else {
										p.sendMessage(
												prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
										return true;
									}
								}
								configPerfil.set(p.getUniqueId() + ".Perfil" + ".Skype", skype);
								p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
										+ args[0].toLowerCase());
								try {
									configPerfil.save(configFile);
								} catch (IOException d) {
									d.printStackTrace();
								}
								return true;
							}
						}
					} else if (!configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						p.sendMessage(prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
						return true;

					}
				}
				// Adicionando skype ----------------------------------- END

				// Adicionando Canal no youtube -----------------------
				if ("youtube".equalsIgnoreCase(args[0])) {
					if (configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						if (p.hasPermission("perfil.criar")) {
							if (args.length >= 2) {
								String canal = "";
								for (int i = 1; i < args.length; i++) {
									canal = canal + args[i] + " ";
								}
								if (getConfig().getBoolean("Use MySQL") == true) {
									if (BD.checar(p.getUniqueId()) == true) {
										BD.add("youtube", canal, p.getUniqueId());
										p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
												+ args[0].toLowerCase());
										return true;
									} else {
										p.sendMessage(
												prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
										return true;
									}
								}
								configPerfil.set(p.getUniqueId() + ".Perfil" + ".Canal", canal);
								p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
										+ args[0].toLowerCase());
								try {
									configPerfil.save(configFile);
								} catch (IOException d) {
									d.printStackTrace();
								}
								return true;
							}
						}
					} else if (!configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						p.sendMessage(prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
						return true;

					}
				}

				// Adicionando canal no youtube ----------------- end

				// Adicionando Nome ------------------------------------

				if ("nome".equalsIgnoreCase(args[0])) {
					if (configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						if (p.hasPermission("perfil.criar")) {
							if (args.length >= 2) {
								String nome = "";
								for (int i = 1; i < args.length; i++) {
									nome = nome + args[i] + " ";
								}
								if (getConfig().getBoolean("Use MySQL") == true) {
									if (BD.checar(p.getUniqueId()) == true) {
										BD.add("nome", nome, p.getUniqueId());
										p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
												+ args[0].toLowerCase());
										return true;
									} else {
										p.sendMessage(
												prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
										return true;
									}
								}
								configPerfil.set(p.getUniqueId() + ".Perfil" + ".NomePorComando", nome);
								p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
										+ args[0].toLowerCase());
								try {
									configPerfil.save(configFile);
								} catch (IOException d) {
									d.printStackTrace();
								}
								return true;
							}
						}
					} else if (!configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						p.sendMessage(prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
						return true;

					}
				}

				// Adicionando Nome ---------------------------- end

				// Adicionando Pensamento --------------------

				if ("pensamento".equalsIgnoreCase(args[0])) {
					if (configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						if (p.hasPermission("perfil.criar")) {
							if (args.length >= 2) {
								String pensa = "";
								for (int i = 1; i < args.length; i++) {
									pensa = pensa + args[i] + " ";
								}
								if (getConfig().getBoolean("Use MySQL") == true) {
									if (BD.checar(p.getUniqueId()) == true) {
										BD.add("pensamento", pensa, p.getUniqueId());
										p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
												+ args[0].toLowerCase());
										return true;
									} else {
										p.sendMessage(
												prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
										return true;
									}
								}
								configPerfil.set(p.getUniqueId() + ".Perfil" + ".Pensamento", pensa);
								p.sendMessage(prefixo + getConfig().getString("Adicionou").replace("&", "§")
										+ args[0].toLowerCase());
								try {
									configPerfil.save(configFile);
								} catch (IOException d) {
									d.printStackTrace();
								}
								return true;
							}
						}
					} else if (!configPerfil.contains(p.getUniqueId() + ".Perfil")) {
						p.sendMessage(prefixo + getConfig().getString("Crie um perfil").replace("&", "§"));
						return true;

					}
				}

				// Adicionando pensamento ---------------------------- end
				if (args.length > 0) {
					if (!"?".equalsIgnoreCase(args[0])) {
						if (sender instanceof Player) {
							if (command.getName().equalsIgnoreCase("perfil")) {
								if (t != null) {
									if (t.getName().equalsIgnoreCase(args[0])) {
										if (configPerfil.contains(t.getUniqueId() + ".Perfil")) {
											if (p.hasPermission("perfil.usar")) {
												// ITENS
												ItemStack skype = new ItemStack(Material.DIAMOND_HELMET, 1);
												ItemMeta skypemeta = skype.getItemMeta();
												if (getConfig().getBoolean("Use MySQL") == false) {
													skypemeta.setDisplayName("§b§lSkype§e:§a§l " + configPerfil
															.getString(t.getUniqueId() + ".Perfil" + ".Skype"));
												} else {
													skypemeta.setDisplayName(
															"§b§lSkype§e:§a§l " + BD.check("skype", t.getUniqueId()));
												}
												skype.setItemMeta(skypemeta);
												ItemStack canal = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
												ItemMeta canalmeta = canal.getItemMeta();
												if (getConfig().getBoolean("Use MySQL") == false) {
													canalmeta.setDisplayName(
															"§6§lCanal§0§l You§c§lTube§e:§a§l " + configPerfil
																	.getString(t.getUniqueId() + ".Perfil" + ".Canal"));
												} else {
													canalmeta.setDisplayName("§6§lCanal§0§l You§c§lTube§e:§a§l "
															+ BD.check("youtube", t.getUniqueId()));
												}
												canal.setItemMeta(canalmeta);
												ItemStack nome = new ItemStack(Material.BONE, 1);
												ItemMeta nomemeta = nome.getItemMeta();
												if (getConfig().getBoolean("Use MySQL") == false) {
													nomemeta.setDisplayName("§6§lNome§e:§a§l " + configPerfil.getString(
															t.getUniqueId() + ".Perfil" + ".NomePorComando"));
												} else {
													nomemeta.setDisplayName(
															"§6§lNome§e:§a§l " + BD.check("nome", t.getUniqueId()));
												}
												nome.setItemMeta(nomemeta);
												ItemStack pensamento = new ItemStack(Material.BOOK_AND_QUILL, 1);
												ItemMeta pensamentometa = pensamento.getItemMeta();
												if (getConfig().getBoolean("Use MySQL") == false) {
													pensamentometa.setDisplayName(
															"§6§lPensamento§e:§a§l " + configPerfil.getString(
																	t.getUniqueId() + ".Perfil" + ".Pensamento"));
												} else {
													pensamentometa.setDisplayName("§6§lPensamento§e:§a§l "
															+ BD.check("pensamento", t.getUniqueId()));
												}
												pensamento.setItemMeta(pensamentometa);
												ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
												SkullMeta m = (SkullMeta) item.getItemMeta();
												m.setOwner(t.getName());
												m.setDisplayName("§a" + t.getName());
												ArrayList<String> lore2 = new ArrayList<>();
												lore2.add("§aEste é o perfil de §e§l" + t.getName() + "§a.");
												lore2.add("§a§lEle pode fazer alterações quando quiser.");
												m.setLore(lore2);
												item.setItemMeta(m);
												ItemStack Espaco = new ItemStack(Material.STAINED_GLASS_PANE, 1,
														(short) 8);
												ItemMeta Espacometa = Espaco.getItemMeta();
												Espacometa.setDisplayName(prefixo);
												Espaco.setItemMeta(Espacometa);
												// ITENS
												Inventory inv = Bukkit.createInventory(null, 9 * 1, getConfig()
														.getString("Titulo do inventario").replace("&", "§"));
												p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 1);
												sender.sendMessage(getConfig().getString("Prefixo").replace("&", "§")
														+ getConfig().getString("Abriu").replace("&", "§")
																.replace("@player", t.getName()));
												if (getConfig().getBoolean("Use MySQL") == false) {
													if (configPerfil.contains(t.getUniqueId() + ".Perfil" + ".Skype")) {
														inv.addItem(skype);
													}
													if (configPerfil.contains(t.getUniqueId() + ".Perfil" + ".Canal")) {
														inv.setItem(2, canal);
													}
													if (configPerfil.contains(
															t.getUniqueId() + ".Perfil" + ".NomePorComando")) {
														inv.setItem(4, nome);
													}
													if (configPerfil
															.contains(t.getUniqueId() + ".Perfil" + ".Pensamento")) {
														inv.setItem(6, pensamento);
													}
												} else {
													if (BD.check("skype", t.getUniqueId()) != null) {
														inv.addItem(skype);
													}
													if (BD.check("youtube", t.getUniqueId()) != null) {
														inv.setItem(2, canal);
													}
													if (BD.check("nome", t.getUniqueId()) != null) {
														inv.setItem(4, nome);
													}
													if (BD.check("pensamento", t.getUniqueId()) != null) {
														inv.setItem(6, pensamento);
													}
												}
												inv.setItem(1, Espaco);
												inv.setItem(3, Espaco);
												inv.setItem(5, Espaco);
												inv.setItem(7, Espaco);
												inv.setItem(8, item);
												p.openInventory(inv);
												return true;

											} else if (!(p.hasPermission("perfil.usar"))) {
												sender.sendMessage(getConfig().getString("Prefixo").replace("&", "§")
														+ getConfig().getString("Sem permissao").replace("&", "§"));
												return true;

											}
										} else if (!configPerfil.contains(t.getUniqueId() + ".Perfil")) {
											p.sendMessage(prefixo + getConfig().getString("Impossivel")
													.replace("&", "§").replace("@player", args[0]));
											return true;
										}
									}
								} else {
									p.sendMessage(prefixo + getConfig().getString("Impossivel").replace("&", "§")
											.replace("@player", args[0]));
									return true;
								}
							}
						}
					}
				}
				if ("?".equalsIgnoreCase(args[0])) {
					p.sendMessage(prefixo + "§a Criado por §d§lKami, guigarciazinho");
					p.sendMessage(prefixo + "§a Use §b/perfil <nome>§a para visualizar o perfil de um jogador");
					p.sendMessage(prefixo + "§a Use §b/perfil youtube <Canal>§a para alterar o canal do seu perfil");
					p.sendMessage(prefixo + "§a Use §b/perfil skype <Skype>§a para alterar o skype do seu perfil");
					p.sendMessage(prefixo
							+ "§a Use §b/perfil pensamento <pensamento>§a para alterar o pensamento do seu perfil");
					p.sendMessage(prefixo + "§a Use §b/perfil nome <nome>§a para alterar o nome do seu perfil");

				}
			}
			sender.sendMessage("§e Criado por §dKami§e. Fórum §fGamer's§b Board");
			return true;
		}

		return false;
	}

	@EventHandler
	public void aoClicarInv(InventoryClickEvent e) {
		if (e.getInventory().getTitle()
				.equalsIgnoreCase(getConfig().getString("Titulo do inventario").replace("&", "§"))) {
			e.setCancelled(true);
		}
	}

}
