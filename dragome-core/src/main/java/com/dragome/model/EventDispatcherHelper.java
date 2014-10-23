/*******************************************************************************
 * Copyright (c) 2011-2014 Fernando Petrola
 * 
 * This file is part of Dragome SDK.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.dragome.model;

import java.util.List;
import java.util.concurrent.Executor;

import com.dragome.annotations.PageAlias;
import com.dragome.commons.DefaultDragomeConfigurator;
import com.dragome.commons.DragomeConfigurator;
import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.annotations.MethodAlias;
import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.debugging.execution.DragomeApplicationLauncher;
import com.dragome.helpers.AnnotationsHelper;
import com.dragome.helpers.AnnotationsHelper.AnnotationContainer.AnnotationEntry;
import com.dragome.services.ServiceLocator;
import com.dragome.services.interfaces.ParametersHandler;

public class EventDispatcherHelper
{
	@MethodAlias(alias= "EventDispatcher.executeMainClass")
	public static void executeMainClass() throws Exception
	{
		ServiceLocator.getInstance().setClientSideEnabled(true);

		ServiceLocator.getInstance().setConfigurator(getConfigurator());

		ParametersHandler parametersHandler= ServiceLocator.getInstance().getParametersHandler();

		String className= parametersHandler.getParameter("class");
		if (className == null || className.trim().length() == 0)
		{
			String requestURL= parametersHandler.getRequestURL();

			List<AnnotationEntry> annotationEntries= AnnotationsHelper.getAnnotationsByType(PageAlias.class).getEntries();
			for (AnnotationEntry annotationEntry : annotationEntries)
			{
				boolean isUnique= annotationEntries.size() == 2 && !"discoverer".equals(annotationEntry.getAnnotationValue());
				if (isUnique || (annotationEntry.getAnnotationKey().equals("alias") && requestURL.contains(annotationEntry.getAnnotationValue())))
					className= annotationEntry.getType().getName();
			}
		}

		launch(className);
	}

	private static void launch(String className) throws Exception
	{
		try
		{
			if (className == null || className.trim().length() == 0)
				System.out.println("Please specify activity class to execute in querystring parameter 'class'");
			else
				new DragomeApplicationLauncher().launch(className);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	private static DragomeConfigurator getConfigurator()
	{
		DragomeConfigurator configurator= new DefaultDragomeConfigurator();
		List<AnnotationEntry> annotationEntries= AnnotationsHelper.getAnnotationsByType(DragomeConfiguratorImplementor.class).getEntries();
		for (AnnotationEntry annotationEntry : annotationEntries)
		{
			if (!annotationEntry.getType().equals(DefaultDragomeConfigurator.class))
				configurator= ServiceLocator.getInstance().getReflectionService().createClassInstance((Class<? extends DragomeConfigurator>) annotationEntry.getType());
		}
		return configurator;
	}

	//	@MethodAlias(alias= "EventDispatcher.getComponentById")
	//	private static void getComponentById(Object event)
	//	{
	//		ScriptHelper.eval("stopEvent(event)", null);
	//		String id2= (String) ScriptHelper.eval("event.currentTarget.getAttribute('data-element-id')", null);
	//		EventDispatcherHelper.getComponentById(id2); //TODO revisar el static
	//	}

	@MethodAlias(alias= "EventDispatcher.onEvent")
	private static void onEvent()
	{
		Object event= ScriptHelper.eval("window.event || arguments.callee.caller.arguments[0]", null);
		Executor executor= ServiceLocator.getInstance().getConfigurator().getExecutionHandler().getExecutor();
		executor.execute(new EventExecutor(event));
	}
	protected static String getEventTargetId(Object event)
	{
		//	ScriptHelper.eval("stopEvent(event)", null);
		Object eventTarget= getEventTarget(event);
		ScriptHelper.put("eventTarget", eventTarget, null);
		String id= (String) ScriptHelper.eval("eventTarget.getAttribute('data-element-id')", null);
		return id;
	}

	protected static Object getEventTarget(Object event)
	{
		ScriptHelper.put("event", event, null);
		return ScriptHelper.eval("event.currentTarget ? event.currentTarget : event.target", null);
	}

	private static boolean processingEvent= false;

	public EventDispatcherHelper()
	{
	}

	private synchronized void runOnlySynchronized(Runnable runnable)
	{
		try
		{
			//TODO revisar esto cuando se ejecuta en el cliente, posible freeze!
			if (!processingEvent)
			{
				processingEvent= true;
				runnable.run();
			}
		}
		finally
		{
			processingEvent= false;
		}
	}

	//	public static VisualComponent getComponentById(String id)
	//	{
	//		return ((VisualComponent) DragomeEntityManager.get(id));
	//	}

	public static Runnable applicationRunner;

	@MethodAlias(alias= "EventDispatcher.runApplication")
	public static void runApplication()
	{
		if (applicationRunner != null)
			applicationRunner.run();
		else
			ScriptHelper.eval("alert('Cannot find any activity to execute, please add annotation @PageAlias(alias= \"page-name\") to your activity class.')", null);
	}

	public static void runApplication(Runnable runnable)
	{
		if (ServiceLocator.getInstance().isRemoteDebugging())
			applicationRunner= runnable;
		else
			runnable.run();
	}
}
