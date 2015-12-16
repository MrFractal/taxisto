
/**
 * Created by max on 06.02.14.
 * 
 * should be used for requesting client credit cards info
 * 
 * 
 * 
 */

@interface CardsRequest : NSObject {
  long id;
}

@property(nonatomic) long id;
- (void) setId:(long)id;
@end
