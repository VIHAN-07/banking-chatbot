import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Chip,
  LinearProgress,
  Avatar,
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Badge,
  IconButton,
  Tooltip,
  Tab,
  Tabs,
  Paper
} from '@mui/material';
import {
  TrendingUp,
  TrendingDown,
  TrendingFlat,
  AccountBalance,
  Analytics,
  NotificationsActive,
  Insights,
  Assessment,
  PieChart,
  Timeline
} from '@mui/icons-material';
import { motion } from 'framer-motion';

interface FinancialInsight {
  category: string;
  insight: string;
  recommendation: string;
  impact: number;
  priority: 'high' | 'medium' | 'low';
  timestamp: string;
}

interface SpendingPattern {
  category: string;
  amount: number;
  percentage: number;
  trend: 'increasing' | 'decreasing' | 'stable';
  comparison: 'above_average' | 'below_average' | 'average';
}

interface Notification {
  id: string;
  title: string;
  message: string;
  type: string;
  timestamp: string;
  read: boolean;
}

const AdvancedDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [insights, setInsights] = useState<FinancialInsight[]>([]);
  const [spendingPatterns, setSpendingPatterns] = useState<SpendingPattern[]>([]);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      
      // Fetch financial insights
      const insightsResponse = await fetch('/api/advanced/financial-insights');
      const insightsData = await insightsResponse.json();
      setInsights(insightsData);

      // Fetch spending patterns
      const spendingResponse = await fetch('/api/advanced/spending-patterns');
      const spendingData = await spendingResponse.json();
      setSpendingPatterns(spendingData);

      // Fetch notifications
      const notificationsResponse = await fetch('/api/advanced/notifications/unread');
      const notificationsData = await notificationsResponse.json();
      setNotifications(notificationsData.notifications || []);
      setUnreadCount(notificationsData.count || 0);

    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'high': return '#f44336';
      case 'medium': return '#ff9800';
      case 'low': return '#4caf50';
      default: return '#2196f3';
    }
  };

  const getTrendIcon = (trend: string) => {
    switch (trend) {
      case 'increasing': return <TrendingUp sx={{ color: '#f44336' }} />;
      case 'decreasing': return <TrendingDown sx={{ color: '#4caf50' }} />;
      case 'stable': return <TrendingFlat sx={{ color: '#ff9800' }} />;
      default: return <TrendingFlat />;
    }
  };

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };

  const renderInsights = () => (
    <Grid container spacing={3}>
      {insights.map((insight, index) => (
        <Grid item xs={12} md={6} key={index}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.1 }}
          >
            <Card sx={{ height: '100%', borderLeft: `4px solid ${getPriorityColor(insight.priority)}` }}>
              <CardContent>
                <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={2}>
                  <Typography variant="h6" component="div">
                    {insight.category.charAt(0).toUpperCase() + insight.category.slice(1)}
                  </Typography>
                  <Chip 
                    label={insight.priority} 
                    size="small" 
                    sx={{ 
                      backgroundColor: getPriorityColor(insight.priority),
                      color: 'white'
                    }}
                  />
                </Box>
                <Typography variant="body2" color="text.secondary" mb={2}>
                  {insight.insight}
                </Typography>
                <Typography variant="body2" mb={2}>
                  <strong>Recommendation:</strong> {insight.recommendation}
                </Typography>
                {insight.impact > 0 && (
                  <Typography variant="body2" color="success.main">
                    <strong>Potential Impact:</strong> ${insight.impact.toFixed(2)}
                  </Typography>
                )}
              </CardContent>
            </Card>
          </motion.div>
        </Grid>
      ))}
    </Grid>
  );

  const renderSpendingPatterns = () => (
    <Grid container spacing={3}>
      <Grid item xs={12}>
        <Card>
          <CardContent>
            <Typography variant="h6" mb={3}>
              <PieChart sx={{ mr: 1, verticalAlign: 'middle' }} />
              Spending Analysis
            </Typography>
            <List>
              {spendingPatterns.map((pattern, index) => (
                <ListItem key={index} divider>
                  <ListItemAvatar>
                    <Avatar sx={{ bgcolor: 'primary.main' }}>
                      {pattern.category.charAt(0).toUpperCase()}
                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText
                    primary={
                      <Box display="flex" justifyContent="space-between" alignItems="center">
                        <Typography variant="subtitle1">
                          {pattern.category.charAt(0).toUpperCase() + pattern.category.slice(1)}
                        </Typography>
                        <Box display="flex" alignItems="center" gap={1}>
                          {getTrendIcon(pattern.trend)}
                          <Typography variant="h6" color="primary">
                            ${pattern.amount.toFixed(2)}
                          </Typography>
                        </Box>
                      </Box>
                    }
                    secondary={
                      <Box>
                        <LinearProgress
                          variant="determinate"
                          value={pattern.percentage}
                          sx={{ mt: 1, mb: 1 }}
                        />
                        <Box display="flex" justifyContent="space-between">
                          <Typography variant="body2" color="text.secondary">
                            {pattern.percentage.toFixed(1)}% of total spending
                          </Typography>
                          <Chip
                            label={pattern.comparison.replace('_', ' ')}
                            size="small"
                            variant="outlined"
                            color={
                              pattern.comparison === 'above_average' ? 'warning' :
                              pattern.comparison === 'below_average' ? 'success' : 'default'
                            }
                          />
                        </Box>
                      </Box>
                    }
                  />
                </ListItem>
              ))}
            </List>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );

  const renderNotifications = () => (
    <Grid container spacing={2}>
      {notifications.length === 0 ? (
        <Grid item xs={12}>
          <Paper sx={{ p: 3, textAlign: 'center' }}>
            <Typography variant="body1" color="text.secondary">
              No new notifications
            </Typography>
          </Paper>
        </Grid>
      ) : (
        notifications.map((notification) => (
          <Grid item xs={12} key={notification.id}>
            <motion.div
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
            >
              <Card sx={{ bgcolor: notification.read ? 'background.paper' : 'action.hover' }}>
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="flex-start">
                    <Box flex={1}>
                      <Typography variant="subtitle1" fontWeight="bold">
                        {notification.title}
                      </Typography>
                      <Typography variant="body2" color="text.secondary" mt={1}>
                        {notification.message}
                      </Typography>
                      <Typography variant="caption" color="text.secondary" mt={1}>
                        {new Date(notification.timestamp).toLocaleString()}
                      </Typography>
                    </Box>
                    {!notification.read && (
                      <Badge color="primary" variant="dot" sx={{ ml: 2 }} />
                    )}
                  </Box>
                </CardContent>
              </Card>
            </motion.div>
          </Grid>
        ))
      )}
    </Grid>
  );

  const tabContent = [
    { label: 'Financial Insights', icon: <Insights />, content: renderInsights() },
    { label: 'Spending Patterns', icon: <Analytics />, content: renderSpendingPatterns() },
    { label: `Notifications (${unreadCount})`, icon: <NotificationsActive />, content: renderNotifications() }
  ];

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <LinearProgress sx={{ width: '50%' }} />
      </Box>
    );
  }

  return (
    <Box sx={{ width: '100%', maxWidth: 1200, mx: 'auto', p: 3 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        <Assessment sx={{ mr: 1, verticalAlign: 'middle' }} />
        Financial Dashboard
      </Typography>
      
      <Paper sx={{ width: '100%', mb: 3 }}>
        <Tabs
          value={activeTab}
          onChange={handleTabChange}
          variant="fullWidth"
          indicatorColor="primary"
          textColor="primary"
        >
          {tabContent.map((tab, index) => (
            <Tab
              key={index}
              label={tab.label}
              icon={tab.icon}
              iconPosition="start"
              sx={{ minHeight: 72 }}
            />
          ))}
        </Tabs>
      </Paper>

      <Box sx={{ mt: 3 }}>
        {tabContent[activeTab].content}
      </Box>
    </Box>
  );
};

export default AdvancedDashboard;
