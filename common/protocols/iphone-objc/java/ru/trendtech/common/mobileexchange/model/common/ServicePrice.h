
@interface ServicePrice : NSObject {
  NSString * service;
  NSString * details;
  float cost;
}

@property(nonatomic, retain) NSString * service;
@property(nonatomic, retain) NSString * details;
@property(nonatomic) float cost;
- (void) setService:(NSString *)service;
- (void) setDetails:(NSString *)details;
- (void) setCost:(float)cost;
@end
